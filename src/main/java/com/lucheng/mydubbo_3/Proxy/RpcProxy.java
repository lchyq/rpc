package com.lucheng.mydubbo_3.Proxy;

import com.lucheng.mydubbo_3.bean.Invoker;
import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.client.Client;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * rpc代理工厂
 * @author lucheng28
 * @date 2020-03-16
 */
@Slf4j
public class RpcProxy {
    //避免外界初始化
    private RpcProxy(){}
    private static InvocationHandler invocationHandler = (proxy, method, args) -> {
        log.error("生成代理...");
        Class[] interfaces = proxy.getClass().getInterfaces();
        String interfaceName = interfaces[0].getName();
        String methodName = method.getName();
        Class[] argsTypes = null;
        if(args != null){
            argsTypes = new Class[args.length];
            for(int i = 0;i < args.length;i++){
                argsTypes[i] = args[i].getClass();
            }
        }
        Client client = new Client();
        return client.send(getRequestMessage(interfaceName,methodName,argsTypes,args)).getResult();
    };
    private static RequestMessage getRequestMessage(String interfaceName,
                                                    String methodName,
                                                    Class[] argsTypes,
                                                    Object[] args){
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setRequestId(UUID.randomUUID().toString());
        Invoker invoker = new Invoker();
        invoker.setClassName(interfaceName);
        invoker.setMethodName(methodName);
        invoker.setArgsType(argsTypes);
        invoker.setArgs(args);
        requestMessage.setInvoker(invoker);
        log.error("请求参数：{}",requestMessage);
        return requestMessage;
    }

    public static Object getInstance(Class<?> clazz){
        log.error("进入代理...");
        return Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},invocationHandler);
    }
}
