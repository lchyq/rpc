package com.lucheng.learn.client_1.netty_learning.privateprotocol.heartbeat.client;

import com.lucheng.learn.client_1.netty_learning.solvezhanbao.seriable.marshalling.client.MarshallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 心跳检测client
 * @author lucheng28
 * @date 2020-03-03
 */
public class HeartBeatClient {
    public static void main(String[] args) {
        new HeartBeatClient().connect();
    }

    public void connect(){
        ExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        EventLoopGroup work = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder())
                                .addLast(MarshallingCodeFactory.buildMarshallingEncoder())
                                .addLast(new ReadTimeoutHandler(50)) //50s没有读取到消息，需要断线重连
                                .addLast(new HeartBeatClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect("localhost",8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            work.shutdownGracefully();
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        connect(); //5s后重新链接，直到链接成功为止
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
