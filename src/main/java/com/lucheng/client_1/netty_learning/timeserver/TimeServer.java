package com.lucheng.client_1.netty_learning.timeserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 基于netty的时间客户端
 */
public class TimeServer {
    public static void main(String[] args) {
        //用于处理网络请求
        EventLoopGroup boss = new NioEventLoopGroup(1);
        //用于处理io读写
        EventLoopGroup work = new NioEventLoopGroup(1);
        //nio辅助启动类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,work) //绑定nio线程组 即 reactor线程组
                .channel(NioServerSocketChannel.class) //作用同nio中 ServerSocketChannel
                .option(ChannelOption.SO_BACKLOG,1024) //设置tcp参数
                .childHandler(new ChildTimeHandler());//设置用于处理io请求的handler

        try{
            //绑定端口同步等待响应结果
            ChannelFuture fu = serverBootstrap.bind("localhost",9090).sync();
            //关闭端口
            fu.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //优雅关闭
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }
}
