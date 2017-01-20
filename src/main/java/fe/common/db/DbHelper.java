package fe.common.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import fe.common.Assert;
import fe.common.exception.DbException;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by fe on 2017/1/20.
 * db工具类
 */
public class DbHelper {

    public static final Logger logger = LoggerFactory.getLogger(DbHelper.class);

    private static QueryRunner queryRunner = new QueryRunner();
    private static DataSource dataSource;

    private final static ColumnListHandler columnListHandler = new ColumnListHandler(){
        @Override
        protected Object handleRow(ResultSet rs) throws SQLException {
            Object obj = super.handleRow(rs);
            if(obj instanceof BigInteger)
                return ((BigInteger)obj).longValue();
            return obj;
        }

    };
    private final static ScalarHandler scalarHandler = new ScalarHandler(){
        @Override
        public Object handle(ResultSet rs) throws SQLException {
            Object obj = super.handle(rs);
            if(obj instanceof BigInteger)
                return ((BigInteger)obj).longValue();
            return obj;
        }
    };

    private final static List<Class<?>> primitiveClasses = new ArrayList<Class<?>>(){{
        add(Long.class);
        add(Integer.class);
        add(String.class);
        add(java.util.Date.class);
        add(java.sql.Date.class);
        add(java.sql.Timestamp.class);
    }};

    private final static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || primitiveClasses.contains(cls) ;
    }

    public synchronized static void init(String url, String user, String pass) {
        Assert.assertNotNull(Arrays.asList(url, user, pass));
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(url);
        mysqlDataSource.setUser(user);
        mysqlDataSource.setPassword(pass);
        dataSource = mysqlDataSource;
    }

    private static Connection getConnection() throws SQLException {
        Assert.assertNotNull(dataSource, "dataSource初始化异常!");
        return dataSource.getConnection();
    }

    public static <T> T query(Class<T> beanClass, String sql, Object...params) {
        Connection connection = null;
        try{
            connection = getConnection();
            return (T)getQueryRunner().query(connection, sql, isPrimitive(beanClass)?scalarHandler:new BeanHandler(beanClass), params);
        }catch(SQLException e){
            throw new DbException(e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    public static <T> List<T> queryList(Class<T> beanClass, String sql, Object...params) {
        Connection connection = null;
        try{
            connection = getConnection();
            return (List<T>)getQueryRunner().query(connection, sql, isPrimitive(beanClass)?scalarHandler:new BeanListHandler(beanClass), params);
        }catch(SQLException e){
            throw new DbException(e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }


    public static int updateSql(String sql, Object... para) {
        Connection connection = null;
        try {
            connection = getConnection();
            return getQueryRunner().update(connection, sql, new ScalarHandler<Object>(), para);
        } catch (SQLException e) {
            throw new DbException(e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    public static int[] batch(String sql, Object[][] params) {
        Connection connection = null;
        try{
            connection = getConnection();
            return getQueryRunner().batch(connection, sql, params);
        }catch(SQLException e){
            throw new DbException(e);
        }finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private static QueryRunner getQueryRunner() {
        return queryRunner;
    }

}
