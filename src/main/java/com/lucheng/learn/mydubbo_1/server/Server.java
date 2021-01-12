package com.lucheng.learn.mydubbo_1.server;

import com.lucheng.learn.mydubbo_1.bean.RequestMessage;
import com.lucheng.learn.mydubbo_1.bean.ResponseMessage;
import com.lucheng.learn.mydubbo_1.registercenter.RegisterMap;
import com.lucheng.learn.mydubbo_1.util.SerializeUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *  rpc 服务提供者
 *  类似于 provider
 */
@Component
public class Server implements ApplicationContextAware {
    private Map<String,String> localMap = new HashMap<>();
    public void openServer(){
        InputStream in = null;
        OutputStream os = null;
        try {
            ServerSocket serverSocket = new ServerSocket(8088);
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("接收到客户端的调用请求");
                //获取调用数据
                in = socket.getInputStream();
                byte[] request = new byte[1024];
                while (in.read(request) > -1){
                    //获取数据
                    byte[] result = getData(request);
                    //返回给客户端
                    os = socket.getOutputStream();
                    os.write(result);
                    os.flush();
                }
                in.close();
                os.close();
            }
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] getData(byte[] bytes) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        //反序列化
        RequestMessage request = (RequestMessage) SerializeUtils.reSerialize(bytes);
        //拿到需要的信息
        String interName = request.getInterName();
        String methodName = request.getMethodName();
        Object[] args = request.getArgs();
        Class[] types = request.getTypes();
        //根据接口名拿到对应的实现类
        Class clazz = Class.forName(localMap.get(interName));
        Method method = clazz.getMethod(methodName,types);
        Object o = method.invoke(clazz.newInstance(),args);
        ResponseMessage<Object> responseMessage = new ResponseMessage<>();
        responseMessage.setResult(o);
        return SerializeUtils.serialize(responseMessage);
    }
    /**
     * 模拟将接口注册到注册中心、
     * 实际的应用场景是解析对应的
     * xml文件来获取接口与实现之间的关系
     */
    public void registerInterface(String interfaceName,String ref){
        //调用注册中心接口
        //内部已经做了异常处理
        RegisterMap.register(interfaceName,ref);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> provider = applicationContext.getBeansWithAnnotation(Service.class);
        for(Map.Entry<String,Object> entry : provider.entrySet()){
            Class[] inter = entry.getValue().getClass().getInterfaces();
            for(Class clazz : inter){
                localMap.put(clazz.getName(),entry.getValue().getClass().getName());
                System.out.println(clazz.getName()+" : " + entry.getValue().getClass().getName());
            }
        }
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
        Server server = (Server) context.getBean("server");
        server.openServer();
    }
}
