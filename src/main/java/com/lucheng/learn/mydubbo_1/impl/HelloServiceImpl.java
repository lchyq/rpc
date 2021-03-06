package com.lucheng.learn.mydubbo_1.impl;

import com.lucheng.learn.mydubbo_1.inter.HelloService;
import org.springframework.stereotype.Service;

/**
 * HelloService 实现类
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String getName(String name) {
        if("lucheng".equalsIgnoreCase(name)){
            return "lucheng";
        }else if("hyq".equalsIgnoreCase(name)){
            return "hyq";
        }
        return "未查询到该名称";
    }
}
