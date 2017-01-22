package fe.common.zk;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by fe on 2017/1/19.
 * zk管理工具 调用入口
 */
public class ZookeeperManager {

    public static final Logger logger = LoggerFactory.getLogger(ZookeeperManager.class);

    private ReadWriteStore readWriteStore;
    private String zkHost;
    private String defaultPrefix;

    public void init(String zkHost,String defaultPrefix,boolean debug) throws Exception {
        try {
            intInternal(zkHost,defaultPrefix,debug);
            logger.info("zk init success....");
        } catch (Exception e) {
            logger.error("zk init error!");
            throw e;
        }
    }

    private void intInternal(String zkHost,String defaultPrefix,boolean debug) throws InterruptedException,IOException,KeeperException {
        logger.info("init param, zkHost :{},defaultPrefix:{}",zkHost,defaultPrefix);
        this.zkHost = zkHost;
        this.defaultPrefix = defaultPrefix;
        readWriteStore = new ReadWriteStore(true);
        readWriteStore.connect(zkHost);
    }

    public static ZookeeperManager getInstance() {
        return SingletonHolder.zookeeperManager;
    }

    private static class SingletonHolder {
        private static ZookeeperManager zookeeperManager = new ZookeeperManager();
    }

}
