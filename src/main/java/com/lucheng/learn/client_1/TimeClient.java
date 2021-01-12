package com.lucheng.learn.client_1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 时间处理客户端
 */
public class TimeClient {
    public static void main(String[] args) {
        //只需要工作线程即可
        EventLoopGroup work = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(work)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect("localhost",8899);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.print("客户端已经与服务端建立连接");
                }
            });
            future.channel().closeFuture().sync();
        }catch (Exception e){
            System.out.print(e);
        }finally {
            work.shutdownGracefully();
        }
    }
}
