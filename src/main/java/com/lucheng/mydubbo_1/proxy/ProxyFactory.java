package com.lucheng.mydubbo_1.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理工厂
 */
public class ProxyFactory {
    private static InvocationHandler handler = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //在具体的代理方法内部实现rpc客户端的远程调用
            System.out.print(proxy);
            return null;
        }
    };

    /**
     * 获取接口的代理类
     * @param clazz
     * @return
     */
    public static Object getInstance(Class clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),handler);
    }
}
