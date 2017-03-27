package fe.common;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by fe on 2017/3/21.
 */
public class NioServer {

    private Selector selector;

    public void init(int port) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        this.selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void listen() throws Exception {
        while (true) {
            selector.select();

            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.write(ByteBuffer.wrap(new String("hello word!").getBytes()));
                    serverSocketChannel.register(this.selector,SelectionKey.OP_READ);

                } else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }

    private void read(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        channel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        String msg = new String(data);
        System.out.println("receive msg :" + new String(data));
        channel.write(ByteBuffer.wrap(msg.getBytes()));
    }

    public static void main(String args[]) throws Exception {
        NioServer nioServer = new NioServer();
        nioServer.init(11668);
        nioServer.listen();

    }


}
