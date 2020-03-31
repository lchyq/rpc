package com.lucheng.mydubbo_3.service;

import java.util.concurrent.CompletableFuture;

/**
 * 异步调用service
 * @author lucheng
 * @date 2020-03-30
 */
public interface HelloFutureService {
    CompletableFuture<String> sayName(String name,int timeout);
}
