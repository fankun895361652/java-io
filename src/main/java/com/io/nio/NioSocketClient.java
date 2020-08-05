package com.io.nio;

import com.io.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Title:
 * @author: fan.kun
 * @date: 2020/8/4
 */
public class NioSocketClient {
    public static void main(String[] args) {
        // Socket 客户端（接收信息并打印）
        try (Socket cSocket = new Socket(InetAddress.getLocalHost(), Constants.PORT)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println("NIO 客户端：" + s));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
