package fe.common.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fe on 2017/1/19.
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

    private AtomicInteger threadNum = new AtomicInteger(1);

    private String prefix;

    private boolean daemon;

    private ThreadGroup threadGroup;

    public NamedThreadFactory() {
        this("pool-" + POOL_SEQ.incrementAndGet(),false);
    }

    public NamedThreadFactory(String prefix) {
        this(prefix,false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        this.prefix = prefix + "-thread-";
        this.daemon = daemon;
        threadGroup = System.getSecurityManager() == null ? Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String threadName = prefix + threadNum.incrementAndGet();
        Thread thread = new Thread(threadGroup,r,threadName);
        thread.setDaemon(daemon);
        return thread;
    }

    public AtomicInteger getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(AtomicInteger threadNum) {
        this.threadNum = threadNum;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public void setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
    }

    public static void main(String[] args) {
        Thread t = new Thread();
//        System.out.println(t.getThreadGroup());
//        System.out.println(Thread.currentThread().getThreadGroup());
//        System.out.println(Thread.currentThread().getThreadGroup().getParent());
//        System.out.println(Thread.currentThread().getThreadGroup().getParent().getParent());
    }
}
