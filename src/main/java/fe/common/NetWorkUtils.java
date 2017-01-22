package fe.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by fe on 2016/12/8.
 */
public class NetWorkUtils {

    public static String getHostIp(){
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            return netAddress.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("getHostAddress error!");
        }
    }

    public static String getHostName(){
        try {
            InetAddress netAddress = InetAddress.getLocalHost();
            return netAddress.getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException("getHostName error!");
        }

    }
}
