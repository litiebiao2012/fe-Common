package fe.common.zk;

import fe.common.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by fe on 2017/1/20.
 * 读写zk操作
 */
public class ReadWriteStore extends ConnectionWatcher {

    public static final Logger logger = LoggerFactory.getLogger(ReadWriteStore.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");

    /**
     * 最大重试次数
     */
    public static int MAX_RETRY_NUM = 3;

    /**
     * 每次重试 间隔时间
     */
    public static int RETRY_PERIOD_SECONDS = 2;

    public ReadWriteStore(boolean debug) {
        super(debug);
    }

    /**
     * zk节点写持久数据
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void write(String path,String value) throws InterruptedException,KeeperException {
        Assert.assertNotNull(Arrays.asList(path,value));
        int retryNum = 0;
        while (true) {
            try {
                Stat stat = zk.exists(path,false);

                //TODO 如果节点不存在
                if (stat == null)
                    zk.create(path,value.getBytes(DEFAULT_CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                else
                    zk.setData(path,value.getBytes(DEFAULT_CHARSET),stat.getVersion());

                break;
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                logger.info("write error, while retry , retryNum : {} , e {}",retryNum+1,e);
                retryNum = retryPolicy(retryNum,e);
            }
        }
    }


    /**
     * zk创建临时节点
     * @param path
     * @param value
     * @param isSequential 是否顺序临时
     */
    public void createEphemeralNode(String path,String value,boolean isSequential) throws InterruptedException,KeeperException {
        Assert.assertNotNull(Arrays.asList(path));

        CreateMode createMode = null;
        if (isSequential)
            createMode = CreateMode.EPHEMERAL_SEQUENTIAL;
        else
            createMode = CreateMode.EPHEMERAL;

        int retryNum = 0;
        while (true) {
            try {
                Stat stat = zk.exists(path,false);

                //TODO 如果节点不存在
                if (stat == null)
                    zk.create(path,value.getBytes(DEFAULT_CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
                else
                    if (StringUtils.isNotEmpty(value)) zk.setData(path,value.getBytes(DEFAULT_CHARSET),stat.getVersion());

                break;
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                logger.info("write error, while retry , retryNum : {} , e {}",retryNum+1,e);
                retryNum = retryPolicy(retryNum,e);
            }
        }
    }


    /**
     * 判断path是否存在
     * @param path
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public boolean pathIsExists(String path) throws InterruptedException,KeeperException {
        Assert.assertNotNull(path);

        int retryNum = 0;
        while (true) {
            try {
                Stat stat = zk.exists(path,false);

                if (stat == null)
                    return false;
                else
                    return true;
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                logger.info("write error, while retry , retryNum : {} , e {}",retryNum+1,e);
                retryNum = retryPolicy(retryNum,e);
            }
        }
    }

    /**
     * 读取节点数据
     * @param path
     * @return
     */
    public String read(String path, Watcher watcher,Stat stat)  throws InterruptedException,KeeperException  {
        byte[] zkData = zk.getData(path,watcher,stat);
        return new String(zkData,DEFAULT_CHARSET);
    }

    /**
     * 获取根节点下所有子节点
     * @param path
     * @return
     */
    public List<String> getRootChildren(String path) {
        return getChildren("/");
    }

    /**
     * 获取指定path下的子节点
     * @param path
     * @return
     */
    public List<String> getChildren(String path) {
        List<String> stringList = new LinkedList<String>();
        try {
            stringList = zk.getChildren(path,false);
            return stringList;
        } catch (KeeperException e) {
            logger.error("getChildren error ,e : {}",e);
        } catch (InterruptedException e) {
            logger.error("getChildren error ,e : {}",e);
        }
        return stringList;
    }

    /**
     * 删除指定节点
     * @param path
     */
    public void deleteNode(String path) {
        try {
             zk.delete(path,-1);
        } catch (KeeperException e) {
            logger.error("delete error ,e : {}",e);
        } catch (InterruptedException e) {
            logger.error("delete error ,e : {}",e);
        }
    }

    private int retryPolicy(int retryNum,KeeperException e) throws InterruptedException,KeeperException {
        //TODO 超过最大重试次数
        int num = retryNum++;
        if (retryNum++ >= MAX_RETRY_NUM)
            throw  e;

        //TODO 根据重试次数递增
        int sec = RETRY_PERIOD_SECONDS * retryNum;
        TimeUnit.SECONDS.sleep(sec);

        return  num;
    }


















}
