package fe.common.alarm;

/**
 * Created by fe on 2017/1/18.
 */
public interface Alarm {

    /**
     * 报警定义
     * @param content 报警内容
     * @param receiver 接收人
     */
    public void alarm(String content,String... receiver);
}
