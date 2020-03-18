package com.lucheng.mydubbo_3.server;
import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.serialization.RpcDecoder;
import com.lucheng.mydubbo_3.serialization.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * rpc远程调用服务端
 * @author lucheng28
 * @date 2020-03-16
 */
@Component
@Slf4j
public class RpcServer implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.error("start server...");
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>() { //必须设置childHandler
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcDecoder(RequestMessage.class))
                                .addLast(new RpcEncoder())
                                .addLast(new ServerHandler());
                    }
                });

        ChannelFuture future = null;
        try {
            future = serverBootstrap.bind(8899).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}
