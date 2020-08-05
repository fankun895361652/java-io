package com.io.aio;

import com.io.Constants;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Title:
 * @author: fan.kun
 * @date: 2020/8/5
 */
public class AioSocketServer {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                AsynchronousChannelGroup channelGroup =null;
                try{
                    channelGroup = AsynchronousChannelGroup.withThreadPool(Executors.newFixedThreadPool(4));
                    AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(channelGroup).bind(new InetSocketAddress(Constants.PORT));
                    server.accept(null, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
                        @Override
                        public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
                            server.accept(null,this );
                            Future<Integer> future = result.write(Charset.defaultCharset().encode("你好，世界"));
                            try {
                                future.get();
                                server.close();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {

                        }
                    });
                    channelGroup.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                }catch (Exception e){
                        e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
