package com.lucheng.learn.client_1.netty_learning.privateprotocol.heartbeat.server;

import com.lucheng.learn.client_1.netty_learning.privateprotocol.heartbeat.bean.Header;
import com.lucheng.learn.client_1.netty_learning.privateprotocol.heartbeat.bean.RequestMessage;
import com.lucheng.learn.client_1.netty_learning.privateprotocol.heartbeat.bean.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeaderBeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RequestMessage requestMessage = (RequestMessage) msg;
        if(requestMessage.getHeader().getType() == 1){
            String HeaderMsg = (String) requestMessage.getBody();
            System.out.print(String.format("收到客户端的心跳消息：{%s} \n", HeaderMsg));
            ResponseMessage responseMessage = build();
            ctx.writeAndFlush(responseMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private ResponseMessage build(){
        ResponseMessage requestMessage = new ResponseMessage();
        Header header = new Header();
        header.setType(1);
        requestMessage.setHeader(header);
        requestMessage.setBody("copy that");
        return requestMessage;
    }
}
