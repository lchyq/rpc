package com.lucheng.learn.aysn_learn;

import java.util.concurrent.*;

/**
 * 基于线程池的异步执行
 */
public class BaseThreadPoolRunning {
    //可用核数
    private static Integer avaliable = Runtime.getRuntime().availableProcessors();
    //线程池
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(avaliable,avaliable * 2,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(5),
            new NamedThreadfactory("BaseThreadPoolRunning"),
            new ThreadPoolExecutor.CallerRunsPolicy());

    //没有返回值
    private static void aysnWithoutReturnValue(){
        long currentTime = System.currentTimeMillis();

        //异步执行A
        pool.execute(() ->{
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName()+" do A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //异步执行B
        pool.execute(() ->{
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName()+" do B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //异步执行结果
        System.out.println(System.currentTimeMillis() - currentTime);
    }
    private static void aysnWithReturnValue() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();

        Future<String> A = pool.submit(() -> {
            Thread.sleep(2000);
            System.out.println("do A");
            return "A";
        });

        Future<String> B = pool.submit(() -> {
            Thread.sleep(2000);
            System.out.println("do B");
            return "B";
        });

        System.out.println(A.get());
        System.out.println(B.get());

        System.out.println(System.currentTimeMillis() - start);
    }

    public static void main(String[] args) throws InterruptedException,ExecutionException {
        aysnWithoutReturnValue();
        aysnWithReturnValue();
    }
}

class NamedThreadfactory implements ThreadFactory{
    private String threadGroupName;

    public NamedThreadfactory(String threadGroupName){
        this.threadGroupName = threadGroupName;
    }
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r,threadGroupName);
    }
}
