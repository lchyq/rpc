package com.lucheng;

import com.lucheng.mydubbo_1.impl.HelloServiceImpl;
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
        HelloService helloService = ProxyFactory.getInstance(HelloService.class);
        //结果是 InvocationHandler.invoke的返回值
        helloService.getName("lucheng");
    }
}
