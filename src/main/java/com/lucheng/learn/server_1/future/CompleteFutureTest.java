package com.lucheng.learn.server_1.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class CompleteFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        runAsync();
        supplyAsync();
        whenComplete();
    }

    /**
     * 异步调用不带返回值
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void runAsync() throws ExecutionException, InterruptedException {
        //不带返回值
        //f1,f2是同时执行的，无论那个先执行结束，等待时间都是 max(f1,f2);
        /***************************************************************************/
        CompletableFuture f1 = CompletableFuture.runAsync(() ->{
            try {
                Thread.sleep(5000);
                System.out.println("线程执行完成");
            } catch (InterruptedException e) {
                log.error("线程执行失败");
            }
        });
        CompletableFuture f2 = CompletableFuture.runAsync(() ->{
            try {
                Thread.sleep(7000);
                System.out.println("线程执行完成");
            } catch (InterruptedException e) {
                log.error("线程执行失败");
            }
        });
        long start;
        long end;
        log.error("开始时间："+(start = System.currentTimeMillis()));
        f1.get();
        f2.get();
        log.error("结束时间："+(end = System.currentTimeMillis()));
        log.error("执行耗时："+(end - start));
        /*************************************************************************/
    }

    /**
     * 异步调用带返回值
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture f1 = CompletableFuture.supplyAsync(() ->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
               log.error("异步执行失败");
            }
            return System.currentTimeMillis();
        });

        CompletableFuture f2 = CompletableFuture.supplyAsync(() ->{
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                log.error("异步执行失败");
            }
            return System.currentTimeMillis();
        });
        long start = System.currentTimeMillis();
        log.error("执行耗时："+ (((long)f2.get()) - start ));
    }

    /**
     *  异步调用并通知结果
     */
    private static void whenComplete(){
        CompletableFuture<String> result = new CompletableFuture<>();
        CompletableFuture completableFuture = CompletableFuture.runAsync(() ->{
            log.error("开始异步调用执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.error("超时异常");
            }
        });
        completableFuture.whenComplete((Void,Throwable) ->{
            result.complete("执行完成");
        });
        try{
            log.error("异步结果："+result.get());
//            Thread.sleep(10000);
        }catch (Exception e){
            log.error("超时异常");
        }
        log.error("主线程执行完成");
    }
}

