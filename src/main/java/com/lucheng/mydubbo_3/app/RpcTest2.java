package com.lucheng.mydubbo_3.app;

import com.lucheng.mydubbo_3.Proxy.RpcProxy;
import com.lucheng.mydubbo_3.service.HelloService;

public class RpcTest2 {
    public static void main(String[] args) {
        HelloService helloService = (HelloService) RpcProxy.getInstance(HelloService.class);
        helloService.write("lucheng");
    }
}
