package com.lucheng.client_1.netty_learning.solvezhanbao.seriable.marshalling.server;

import com.lucheng.client_1.netty_learning.solvezhanbao.seriable.java.server.OrderServerHandler;
import com.lucheng.client_1.netty_learning.solvezhanbao.seriable.marshalling.client.MarshallingCodeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class MarshallingServer {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>() { //必须设置childHandler
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder())
                                .addLast(MarshallingCodeFactory.buildMarshallingEncoder())
                                .addLast(new OrderServerHandler());
                    }
                });

        ChannelFuture future = null;
        try {
            future = serverBootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
