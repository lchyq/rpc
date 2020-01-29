package com.lucheng.mydubbo_1.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理工厂
 */
public class ProxyFactory {
    public static InvocationHandler handler = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //在具体的代理方法内部实现rpc客户端的远程调用
            System.out.print("qwe");
            return null;
        }
    };

    /**
     * 获取接口的代理类
     * @param clazz
     * @return
     * 泛型方法
     */
    public static <T> T getInstance(Class<T> clazz){
        // clazz 若是接口则代理类需要的接口为：new Class[]{clazz}
        //不是 clazz.getInterfaces() --> clazz 是接口的实现类时可以使用
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},handler);
    }
}
