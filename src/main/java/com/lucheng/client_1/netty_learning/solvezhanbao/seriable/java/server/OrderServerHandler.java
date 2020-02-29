package com.lucheng.client_1.netty_learning.solvezhanbao.seriable.java.server;

import com.lucheng.client_1.netty_learning.solvezhanbao.seriable.java.bean.OrderRequest;
import com.lucheng.client_1.netty_learning.solvezhanbao.seriable.java.bean.OrderResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class OrderServerHandler extends ChannelInboundHandlerAdapter {
    private int count;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.print(3);
        OrderRequest orderRequest = (OrderRequest) msg;
        System.out.print(String.format("收到客户端的订单请求：{%s}，count:{%s}", orderRequest, ++count));
        ctx.writeAndFlush(getResponse(orderRequest.getOrderId()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private OrderResponse getResponse(int i){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(i);
        orderResponse.setOrderStatus("购买成功");
        return orderResponse;
    }
}
