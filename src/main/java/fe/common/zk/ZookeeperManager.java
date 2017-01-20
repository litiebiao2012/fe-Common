package fe.common.zk;

/**
 * Created by fe on 2017/1/19.
 * zk管理工具 调用入口
 */
public class ZookeeperManager {
    public static String name = "aaa";

    public static ZookeeperManager getInstance() {
        return SingletonHolder.zookeeperManager;
    }

    private static class SingletonHolder {
        private static ZookeeperManager zookeeperManager = new ZookeeperManager();
    }

    public static void main(String[] args) {
        System.out.println(ZookeeperManager.getInstance());
    }

}
