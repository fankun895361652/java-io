package com.io.bio;

import com.io.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @Title:
 * @author: fan.kun
 * @date: 2020/8/4
 */
public class BioSocketClient {
    public static void main(String[] args) {
        //绑定端口
        try (Socket cSocket = new Socket(InetAddress.getLocalHost(), Constants.PORT)) {
            //从绑定的这个socket读取数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
            bufferedReader.lines().forEach(s -> System.out.println("客户端：" + s));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
