package com.lucheng.mydubbo_3.impl;

import com.lucheng.mydubbo_3.service.HelloFutureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 异步实现类
 * @author lucheng
 * @date 2020-03-30
 */
@Service
@Slf4j
public class HelloFutureServiceImpl implements HelloFutureService {
    @Override
    public CompletableFuture<String> sayName(String name,int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            log.error("中断异常",e);
        }
        return CompletableFuture.completedFuture(name);
    }
}
