package com.io.nio;

import com.io.Constants;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Title:
 * @author: fan.kun
 * @date: 2020/8/4
 */
public class NioSocketServer {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 4,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                //打开调度器
                try (Selector selector = Selector.open();
                    //打开socket通道
                    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();) {
                    //绑定socket地址
                    serverSocketChannel.bind(new InetSocketAddress(InetAddress.getLocalHost(), Constants.PORT));
                    //设置未非阻塞
                    serverSocketChannel.configureBlocking(false);
                    //将此channel注册大豆selector
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    while (true) {
                        // 阻塞等待就绪的Channel
                        selector.select();
                        //获取到selector中所有就绪的selectKeys实例，每有一个ServerSocketChannel注册到selector 就会产生一个key

                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            try (SocketChannel channel = ((ServerSocketChannel) key.channel()).accept()) {
                                channel.write(Charset.defaultCharset().encode("你好，世界"));
                            }
                            //将就绪的selectKey移除，防止重复
                            iterator.remove();
                            try{
                                //可接收状态
                                if(key.isAcceptable()){
                                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                                    //设置未非阻塞
                                    channel.configureBlocking(false);
                                    //将此channel注册到selector
                                    channel.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ);
                                }
                                //可读
                                if(key.isReadable()){
                                        SocketChannel socketChannel = (SocketChannel) key.channel();
                                       ByteBuffer out = (ByteBuffer) key.attachment();
                                       //从channel读取数据存入到ByteBuffer里面
                                        socketChannel.read(out);
                                }
                                //可写
                                if(key.isWritable()){
                                    SocketChannel socketChannel = (SocketChannel) key.channel();
                                    ByteBuffer out = (ByteBuffer) key.attachment();
                                    out.flip();
                                    //将ByteBuffer数据写入到channel
//                                    socketChannel.write(out);
                                    socketChannel.write(Charset.defaultCharset().encode("你好，世界"));
                                    out.compact();
                                }
                            }catch (Exception e){
                                    e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
