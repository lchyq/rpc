package com.lucheng.mydubbo_2.impl;

import com.lucheng.mydubbo_2.inter.HelloService;
import org.springframework.stereotype.Service;

/**
 * HelloService 实现类
 */
@Service
public class HelloServiceImpl_2 implements HelloService {
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
