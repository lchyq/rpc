package com.lucheng.mydubbo_3.client;
import com.lucheng.mydubbo_3.bean.RequestMessage;
import com.lucheng.mydubbo_3.bean.ResponseMessage;
import com.lucheng.mydubbo_3.serialization.RpcDecoder;
import com.lucheng.mydubbo_3.serialization.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @author lucheng28
 * @date 2020-03-16
 * 远程调用rpc客户端
 */
@Slf4j
public class RpcClient extends ChannelInboundHandlerAdapter {
    private ResponseMessage responseMessage;
    private final CountDownLatch lock = new CountDownLatch(1);
    public ResponseMessage send(RequestMessage requestMessage){
        log.error("client send");
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
                            ch.pipeline().addLast(new RpcDecoder(ResponseMessage.class))
                            .addLast(new RpcEncoder())
                            .addLast(RpcClient.this);
                        }
                    });
            ChannelFuture future = bootstrap.connect("localhost",8899).sync();
            future.addListener((ChannelFutureListener) future1 -> log.error("rpc远程调用客户端已经与服务端建立连接"));
            future.channel().writeAndFlush(requestMessage).sync();
            log.error("发送完成");
            if(this.responseMessage == null){
                log.error("响应为空");
                    log.error("wait...");
                    lock.await();

            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            work.shutdownGracefully();
        }
        log.error("准备返回得结果：{}",responseMessage);
        return responseMessage;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.responseMessage = (ResponseMessage) msg;
        log.error("服务端响应结果：{}",responseMessage);
        lock.countDown();
        log.error("服务端消息处理完毕");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("rpc客户端远程调用失败 e:{}",cause.getMessage());
        ctx.close();
    }
}
