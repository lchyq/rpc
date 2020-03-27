package com.lucheng.mydubbo_3.Util;

import com.lucheng.mydubbo_3.bean.ResponseMessage;
import com.lucheng.mydubbo_3.client.ClientChannelHandler;
import com.lucheng.mydubbo_3.client.ClientTransport;
import com.lucheng.mydubbo_3.serialization.RpcDecoder;
import com.lucheng.mydubbo_3.serialization.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * ClientTransport 工厂类
 * 负责创建 client需要的transport
 * @author lucheng
 * @date 2020-03-26
 */
@Slf4j
public class ClientTransportFactory {

    private ClientTransportFactory(){}
    /**
     * 服务端接口 与 客户端 长连接map映射
     * 对于consumer 公用 同一个接口下的tcp长连接
     */
    private static final Map<String, ClientTransport> clientTransportMap = new ConcurrentHashMap<>();

    /**
     * 获取服务连接锁
     * 避免多线程创建clientTransPort
     */
    private static final Map<String,Object> clientTransPortInitLock = new ConcurrentHashMap<>();

    /**
     * 客户端连接重试次数
     * 默认重连三次
     */
    private static final Map<String,Integer> clientReConTimes = new ConcurrentHashMap<>();

    /**
     * 获取 ClientTransPort
     * @param clientTransPortKey ip+port
     * @return ClientTransPort
     * @throws Exception ex
     */
    public static ClientTransport getClientTransPort(String clientTransPortKey) throws Exception{
        if(StringUtils.isEmpty(clientTransPortKey) ||
            StringUtils.isBlank(clientTransPortKey)){
            throw new RuntimeException("获取客户端连接失败，客户端鉴权key为null");
        }
        ClientTransport clientTransport = clientTransportMap.get(clientTransPortKey);
        if(clientTransport == null){
            //需要初始化建立client 与 provider之间的连接
            Object lock = clientTransPortInitLock.computeIfAbsent(clientTransPortKey,k -> new Object());
            //避免多线程创建
            synchronized (lock){
                //double check
                if(clientTransport == null){
                    Channel channel = buildChannel(clientTransPortKey);
                    clientTransport = new ClientTransport();
                    clientTransport.setChannel(channel);
                    clientTransportMap.put(clientTransPortKey,clientTransport);
                    buildHandler(channel,clientTransport);
                }
            }
        }
        return clientTransport;
    }

    /**
     * 客户端连接
     * @param clientTransPortKey key
     * @return channel
     * @throws InterruptedException ex
     */
    private static Channel buildChannel(String clientTransPortKey) throws InterruptedException {
        Channel channel = null;
        //只需要工作线程即可
        EventLoopGroup work = new NioEventLoopGroup();
        channel = connect(work,clientTransPortKey);
        return channel;
    }

    /**
     * 客户端连接操作，包括失败重试机制
     * @param work work
     * @param clientTransPortKey client
     * @return channel
     * @throws InterruptedException ex
     */
    private static Channel connect(EventLoopGroup work,String clientTransPortKey) throws InterruptedException {
        int reConTime = clientReConTimes.computeIfAbsent(clientTransPortKey,k -> 0);
        if(++reConTime > 3){
            clientReConTimes.remove(clientTransPortKey);
            log.error("客户端连接失败！");
            throw new RuntimeException("客户端连接失败！");
        }
        if(reConTime > 1){
            log.error("客户端 获取服务端连接失败 开始重试新连接");
        }
        Channel channel = null;
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcDecoder(ResponseMessage.class))
                                .addLast(new RpcEncoder())
                                .addLast("CLIENT_CHANNEL_HANDLER",new ClientChannelHandler());
                    }
                });
        ChannelFuture future = bootstrap.connect("localhost", 8899);
        //等待连接 默认超时时间为 5s
        future.await(5000, TimeUnit.MILLISECONDS);
        if(future.isSuccess()){
            channel = future.channel();
            log.error("客户端 获取到 服务端连接！");
        }else{
            connect(work, clientTransPortKey);
        }
        return channel;
    }

    /**
     * 为hanlder 添加clientTransport
     * @param channel
     * @param clientTransport
     */
    private static void buildHandler(Channel channel,ClientTransport clientTransport){
        ClientChannelHandler clientChannelHandler = (ClientChannelHandler) channel.pipeline().get("CLIENT_CHANNEL_HANDLER");
        clientChannelHandler.setClientTransport(clientTransport);
    }
}
