package com.lucheng;

import com.lucheng.mydubbo_1.inter.HelloService;
import com.lucheng.mydubbo_1.proxy.ProxyFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
        HelloService  helloService = (HelloService) ProxyFactory.getInstance(HelloService.class);

//        helloService.getName("lucheng");
    }
}
