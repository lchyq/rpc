package com.lucheng.learn.mydubbo_1.registercenter;

import com.lucheng.mydubbo_3.exception.RpcException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟注册中心接口与实现类之间的映射
 * 此处忽略 ip，端口 等计算机细节
 * 仅仅存储接口之间的映射
 */
@Component
public class RegisterMap implements ApplicationContextAware {
    private static final Map<String,Object> registerService = new ConcurrentHashMap<>();

    /**
     * 模拟 <jsf:provider interface="xxx" ref = "xxx"></>
     * @param interfaceName
     * @param ref
     */
    public static void register(String interfaceName,String ref){
        if(interfaceName == null || interfaceName.length() <= 0){
            throw new RpcException("请输入接口名称！！！");
        }
        try {
            Class clazz = Class.forName(ref);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RpcException("接口发布到注册中心失败！！");
        }
        //注册到注册中心
        registerService.put(interfaceName,ref);
    }

    /**
     *  获取到 provider 列表
     * @return
     */
    public static Map<String,Object> getProviderList(){
        if(registerService.size() <= 0){
            return new HashMap<>();
        }
        return new HashMap<>(registerService);
    }

    /**
     * 模拟项目启动时 接口注册到注册中心
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> providers = applicationContext.getBeansWithAnnotation(Service.class);
        for(Map.Entry entry : providers.entrySet()){
            Class[] inter = entry.getValue().getClass().getInterfaces();
            for(Class cl : inter){
                    registerService.put(cl.getName(),entry.getValue());
                    System.out.print(String.format("接口{%s} 注册到注册中心，impl为{%s}",cl.getName(),entry.getValue().getClass().getName()));
                }
        }
    }
}
