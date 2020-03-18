package com.lucheng.mydubbo_3.Util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lucheng28
 * @date 2020-03-12
 * 模拟注册中心
 */
@Component
public class RegisterCenter {
    private static final Map<String,Object> localMaps = new ConcurrentHashMap<>();
    public static void register(String interName,Object instance){
        if(StringUtils.isNotEmpty(interName) && StringUtils.isNotBlank(interName)){
            localMaps.put(interName,instance);
        }
    }
    public static Object getInterface(String interName){
        return localMaps.get(interName);
    }

//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        Map<String,Object> localMap = applicationContext.getBeansWithAnnotation(Service.class);
//        for(Map.Entry<String,Object> entry : localMap.entrySet()){
//            Class[] inters = entry.getValue().getClass().getInterfaces();
//            for(Class c : inters){
//                localMap.put(c.getName(),entry.getValue());
//                System.out.print(String.format("接口{%s} 注册到注册中心，impl为{%s}",c.getName(),entry.getValue().getClass().getName()));
//
//            }
//        }
//    }
}
