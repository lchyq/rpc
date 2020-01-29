package com.lucheng.server_1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 服务器实例
 * 应用响应的handler来处理请求
 * @date 2019-01-16
 * @author lucheng28
 */
public class MyServer {
    /**
     * 端口号
     */
    private int port;

    /**
     * 构造方法
     * @param port
     */
    public MyServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        /**
         * boss 线程池
         * 用来接收请求的链接
         */
        EventLoopGroup boss = new NioEventLoopGroup();
        /**
         * worker 线程池
         * 用来处理请求
         */
        EventLoopGroup worker = new NioEventLoopGroup();

        //辅助启动类
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class) //用来处理新连接进来的channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {// 添加自己的处理类

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new MyHandler())
                                    .addLast(new EchoHandler())
                                    .addLast(new TimeHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128) //连接队列长度为128
                    .childOption(ChannelOption.SO_KEEPALIVE,true); //保持长连接，避免频繁的io操作

            //绑定端口，开始接受进来的链接
            ChannelFuture f = serverBootstrap.bind(port).sync();

            //关闭
            f.channel().closeFuture().sync();
        }finally {
            /**
             * 优雅关闭服务器
             */
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new MyServer(8899).run();
    }
}
