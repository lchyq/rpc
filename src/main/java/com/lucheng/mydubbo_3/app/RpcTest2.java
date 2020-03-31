package com.lucheng.mydubbo_3.app;

import com.lucheng.mydubbo_3.Proxy.RpcProxy;
import com.lucheng.mydubbo_3.service.HelloFutureService;
import com.lucheng.mydubbo_3.service.HelloService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class RpcTest2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(int i = 0;i < 10;i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    HelloFutureService helloService = (HelloFutureService) RpcProxy.getInstance(HelloFutureService.class);
                    CompletableFuture<String> future = helloService.sayName("lucheng",5000);
                    try {
                        log.error("rpc调用结果为："+future.get());
                    }catch (Exception e){
                        log.error("rpc调用失败",e);
                    }
                }
            });
        }
    }
}
