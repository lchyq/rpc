package com.lucheng.mydubbo_3.impl;

import com.lucheng.mydubbo_3.service.HelloService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author lucheng28
 * @date 2020-03-13
 */
@Service
public class HelloServiceImpl_3 implements HelloService {
    @Override
    public String write(String name) {
        return name;
    }
}
