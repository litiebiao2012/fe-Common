package fe.common.zk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

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






}
