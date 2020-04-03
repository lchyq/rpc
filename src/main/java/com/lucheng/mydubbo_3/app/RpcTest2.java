package com.lucheng.mydubbo_3.app;

import com.lucheng.mydubbo_3.Proxy.RpcProxy;
import com.lucheng.mydubbo_3.service.HelloFutureService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class RpcTest2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        long end;
        log.error("main线程并发调用开始时间："+ start );
        for(int i = 0;i < 10;i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    HelloFutureService helloService = (HelloFutureService) RpcProxy.getInstance(HelloFutureService.class);
                    CompletableFuture<String> future = helloService.sayName("lucheng",2000);
                    log.error(Thread.currentThread().getName()+": 异步调用完成，等待结果返回");
                    try {
                        log.error(Thread.currentThread().getName()+": rpc调用结果为："+future.get());
                    }catch (Exception e){
                        log.error("rpc调用失败",e);
                    }
                }
            });
        }
        end = System.currentTimeMillis();
        log.error("main线程并发调用完成耗时："+(end - start));
    }
}
