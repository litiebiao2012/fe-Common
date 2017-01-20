package fe.common.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fe on 2017/1/19.
 * zookeeper连接实现
 */
public class ConnectionWatcher implements Watcher {
    public static final Logger logger = LoggerFactory.getLogger(ConnectionWatcher.class);

    /**
     * session超时时间
     */
    private static final int SESSION_TIMEOUT = 10;

    /**
     *
     */
    private static final int CONNECT_TIMEOUT = 3;

    private static transient String zkHost;

    private boolean debug = false;

    private ReentrantLock reentrantLock = new ReentrantLock();

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    protected ZooKeeper zk;

    public ConnectionWatcher() {
    }

    public ConnectionWatcher(boolean debug) {
        this.debug = debug;
    }

    private void connect(String host) throws IOException,InterruptedException{
        zkHost = host;
        zk = new ZooKeeper(zkHost,SESSION_TIMEOUT,this);
        countDownLatch.await(CONNECT_TIMEOUT,TimeUnit.SECONDS);
        logger.info("zk connect!");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        logger.info("watchedEvent KeeperState : {}",watchedEvent.getState());
        //TODO 与zk建立上连接
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            logger.info("zk syncConnected!");
            countDownLatch.countDown();
        }

        if (watchedEvent.getState() == Event.KeeperState.Expired) {
            logger.error("zk expired");
            if (!debug) {
                reconnect();
            }
        }

        if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
            logger.error("zk disconnected!");
        }

        if (watchedEvent.getState() == Event.KeeperState.AuthFailed) {
            logger.error("zk authFailed!");
        }

    }

    /**
     * 重试
     */
    public void reconnect() {
        logger.info("start to reconnect zk.....");
        try  {
            reentrantLock.lock();
            while (true) {
                try {
                    if (!zk.getState().equals(ZooKeeper.States.CLOSED))
                        break;

                    close();

                    connect(zkHost);
                } catch (Exception e) {
                    logger.error("reconnect error， e : {}",e);
                    logger.info("sleep : {}",ReadWriteStore.RETRY_PERIOD_SECONDS);
                    try {
                        TimeUnit.SECONDS.sleep(ReadWriteStore.RETRY_PERIOD_SECONDS);
                    } catch (InterruptedException e1) {
                        //TODO ignore
                    }
                }
            }
            logger.info("reconnect success!");
        } finally {
            reentrantLock.unlock();
        }
    }

    private void close() throws InterruptedException {
        zk.close();
    }

}
