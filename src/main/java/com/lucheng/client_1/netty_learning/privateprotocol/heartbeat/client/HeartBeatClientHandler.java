package com.lucheng.client_1.netty_learning.privateprotocol.heartbeat.client;
import com.lucheng.client_1.netty_learning.privateprotocol.heartbeat.bean.Header;
import com.lucheng.client_1.netty_learning.privateprotocol.heartbeat.bean.RequestMessage;
import com.lucheng.client_1.netty_learning.privateprotocol.heartbeat.bean.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

/**
 * 心跳检测客户端 handler
 */
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //每隔5秒发送一次心跳信息
        ctx.executor().scheduleAtFixedRate(new HeaderRunnable(ctx),0,5000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ResponseMessage requestMessage = (ResponseMessage) msg;
        if(requestMessage.getHeader().getType() == 1){
            String headerMsg = (String) requestMessage.getBody();
            System.out.print(String.format("收到服务器的心跳消息：{%s} \n", headerMsg));
        }else{
            //处理其他类型的消息
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();;
        ctx.close();
    }

    private static class HeaderRunnable implements Runnable{
        private ChannelHandlerContext ctx;
        public HeaderRunnable(ChannelHandlerContext channelHandlerContext){
            this.ctx = channelHandlerContext;
        }
        @Override
        public void run() {
            RequestMessage requestMessage = build();
            ctx.writeAndFlush(requestMessage);
        }

        private RequestMessage build(){
            RequestMessage requestMessage = new RequestMessage();
            Header header = new Header();
            header.setType(1);
            requestMessage.setHeader(header);
            requestMessage.setBody("i am here");
            return requestMessage;
        }
    }
}

