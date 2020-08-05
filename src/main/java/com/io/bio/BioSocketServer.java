package com.io.bio;

import com.io.Constants;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Title:
 * @author: fan.kun
 * @date: 2020/8/4
 */
public class BioSocketServer {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //将ServerSocket绑定到指定的端口
                    ServerSocket serverSocket = new ServerSocket(Constants.PORT);
                    while (true){
                        //阻塞直到收到新的客户端连接
                        final Socket socket = serverSocket.accept();
                        Thread thread1 = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try (PrintWriter printWriter = new PrintWriter(socket.getOutputStream())){
                                    //写数据
                                    printWriter.write("你好呀");
                                    printWriter.flush();
                                }catch (Exception e){
                                        e.printStackTrace();
                                }
                            }
                        });
                        thread1.start();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}
