package fe.common.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fe on 2017/1/19.
 * zookeeper连接实现
 */
public class ConnectionWatcher implements Watcher {
    public static final Logger logger = LoggerFactory.getLogger(ConnectionWatcher.class);
    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
