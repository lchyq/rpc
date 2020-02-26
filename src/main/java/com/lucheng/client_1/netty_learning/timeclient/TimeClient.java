package com.lucheng.client_1.netty_learning.timeclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 基于nio的时间服务器
 */
public class TimeClient {
    public static void main(String[] args) {
        EventLoopGroup work = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(work)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //初始化时创建handler 用于处理io请求
                        ch.pipeline().addLast(new TimeServerChannelHandler());
                    }
                });

        try {
            ChannelFuture f = bootstrap.connect("localhost",9090).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
           e.printStackTrace();
        }finally {
            work.shutdownGracefully();
        }
    }
}
