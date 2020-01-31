package com.lucheng.mydubbo_1.client;
import com.lucheng.mydubbo_1.bean.ResponseMessage;
import com.lucheng.mydubbo_1.inter.HelloService;
import com.lucheng.mydubbo_1.proxy.ProxyFactory;
import com.lucheng.mydubbo_1.util.SerializeUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * rpc 调用客户端
 */
public class Client {
    //暂时指定为确定的端口号
    private static int port = 8088;
    //暂时指定为确定的 ip
    private static String host = "127.0.0.1";
    public static ResponseMessage send(byte[] request){
        try {
            Socket socket = new Socket(host,port);
            System.out.println("与服务器建立tcp链接，开始发送请求");
            //发送调用请求
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(request);
            //获取远程调用的结果
            InputStream inputStream = socket.getInputStream();
            byte[] result = new byte[1024];
            inputStream.read(result);
            socket.close();
            //将结果反序列化拿到我们需要的结果
            return (ResponseMessage) SerializeUtils.reSerialize(result);
        } catch (IOException e) {
            System.out.println("客户端链接失败");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
        HelloService helloService = ProxyFactory.getInstance(HelloService.class);
        //结果是 InvocationHandler.invoke的返回值
        String result = helloService.getName("hyq");
        System.out.println(result);
    }
}
