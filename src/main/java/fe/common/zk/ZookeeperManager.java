package fe.common.zk;

import fe.common.Assert;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public void makeDir(String path,String value) {
        try {
            boolean flag = readWriteStore.pathIsExists(path);
            if (!flag)
                readWriteStore.write(path,value);
        } catch (KeeperException e) {
            logger.error("can not write value!, e : {}",e);
        } catch (Exception e) {
            logger.error("can not write value!, e : {}",e);
        }

    }

    public void release() throws InterruptedException {
        readWriteStore.release();
    }

    public List<String> getRootChildren() {
        return readWriteStore.getRootChildren();
    }

    public List<String> getChildren(String path) {
        Assert.assertNotNull(path);
        return readWriteStore.getChildren(path);
    }

    public void writePersistentUrl(String path, String value) throws Exception {
        Assert.assertNotNull(Arrays.asList(path,value));
        readWriteStore.write(path,value);
    }

    public void createEphemeralNode(String path,String value,boolean isSequential) throws Exception {
        Assert.assertNotNull(Arrays.asList(path,value));
        readWriteStore.createEphemeralNode(path,value,isSequential);
    }

    public boolean pathIsExists(String path) throws Exception {
        Assert.assertNotNull(path);
        return readWriteStore.pathIsExists(path);
    }

    public String read(String path, Watcher watcher, Stat stat) throws Exception {
        return readWriteStore.read(path,watcher,stat);
    }

    public void deleteNode(String path) {
        Assert.assertNotNull(path);
        readWriteStore.deleteNode(path);
    }



    public void reconnect() {
        readWriteStore.reconnect();
    }



}
