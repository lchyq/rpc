package com.lucheng.client_1.netty_learning.solvezhanbao.seriable.marshalling.client;

import com.lucheng.client_1.netty_learning.solvezhanbao.seriable.java.client.OrderClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class MarshallingClient {
    public static void main(String[] args) {
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
                                .addLast(new OrderClientHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect("localhost",8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            work.shutdownGracefully();
        }
    }
}
