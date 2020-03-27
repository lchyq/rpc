package com.lucheng.mydubbo_3.client;

import com.lucheng.mydubbo_3.Util.ClientTransportFactory;
import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.bean.ResponseMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * 客户端主要代码
 * 主要实现了 客户端 连接初始化
 * 客户端 同步/异步发送等
 *
 * @author lucheng
 * @date 2020-03-26
 */
@Slf4j
public class Client {
    //客户端传输层
    private ClientTransport clientTransport;
    //客户端是否被销毁
    private volatile boolean destory = false;
    //客户端是否已经初始化
    private volatile boolean init = false;
    //模拟 provider 服务提供者的地址
    private static final String PROVIDER_KEY = "localhost:8899";
    //客户端调用超时时间
    private Integer timeOut = 3000;

    //同步发送
    public ResponseMessage send(Object msg) throws InterruptedException, ExecutionException, TimeoutException {
        RequestMessage requestMessage = (RequestMessage) msg;
        //初始化客户端与服务端之间的连接
        initConnect();
        return clientTransport.sendMsg(requestMessage,timeOut);
    }

    //异步发送
    public ResponseMessage sendAysc(Object msg){
        RequestMessage requestMessage = (RequestMessage) msg;
        //初始化客户端与服务端之间的连接
        initConnect();
        return clientTransport.sendAysc(requestMessage);
    }

    //客户端初始化连接
    private void initConnect(){
        if(destory){
            throw new RuntimeException("客户端已被销毁");
        }
        if(init){
            //说明客户端已初始化 无需重复初始化
            return;
        }
        try {
            clientTransport = ClientTransportFactory.getClientTransPort(PROVIDER_KEY);
            init = true;
        } catch (Exception e) {
            log.error("客户端连接服务端失败！");
        }
    }
}
