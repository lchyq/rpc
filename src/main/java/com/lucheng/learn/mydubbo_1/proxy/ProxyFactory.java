package com.lucheng.learn.mydubbo_1.proxy;

import com.lucheng.learn.mydubbo_1.bean.RequestMessage;
import com.lucheng.learn.mydubbo_1.client.Client;
import com.lucheng.learn.mydubbo_1.util.SerializeUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理工厂
 */
public class ProxyFactory {
    private static InvocationHandler handler = new InvocationHandler() {
        /**
         * 在具体的代理方法内部实现rpc客户端的远程调用
         * @param proxy 代理类
         * @param method
         * @param args
         * @return
         * @throws Throwable
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //获取接口名
             Class[] interfaces = proxy.getClass().getInterfaces();
             //单接口
             String interName = interfaces[0].getName();
            System.out.println("interName: "+interName);
             //获取方法名
            String  methodName = method.getName();
            //参数类型
            Class[] type = null;
            if(args != null){
                 type = new Class[args.length];
                for(int i = 0;i < args.length;i++){
                    type[i] = args[i].getClass();
                }
            }
            //构造请求参数
            RequestMessage message = new RequestMessage.RequestMessageBuilder()
                    .buildInterName(interName)
                    .buildMethodName(methodName)
                    .buildArgs(args)
                    .buildTypes(type)
                    .build();
            //参数序列化
            byte[] request = SerializeUtils.serialize(message);
            //客户端发送请求
            //此处为阻塞获取调用结果
            return Client.send(request).getResult();
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
