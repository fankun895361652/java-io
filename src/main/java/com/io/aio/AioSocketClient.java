package com.io.aio;

import com.io.Constants;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Title:
 * @author: fan.kun
 * @date: 2020/8/5
 */
public class AioSocketClient {
    public static void main(String[] args) throws InterruptedException {
        AsynchronousSocketChannel client = null;
        try {
            client = AsynchronousSocketChannel.open();
            Future<Void> future = client.connect(new InetSocketAddress(InetAddress.getLocalHost(), Constants.PORT));
            future.get();
            ByteBuffer buffer = ByteBuffer.allocate(100);
            AsynchronousSocketChannel finalClient = client;
            client.read(buffer, null, new CompletionHandler<Integer, Void>() {
                @Override
                public void completed(Integer result, Void attachment) {
                    System.out.println("客户端打印：" + new String(buffer.array()));
                }

                @Override
                public void failed(Throwable exc, Void attachment) {
                    exc.printStackTrace();
                    try {
                        finalClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        Thread.sleep(10 * 1000);
    }

}
