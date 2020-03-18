package com.lucheng.mydubbo_3.serialization;

import com.lucheng.mydubbo_3.Util.SerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * rpc调用解码类
 * @author lucheng28
 * @date 2020-03-14
 */
@Slf4j
public class RpcDecoder extends ByteToMessageDecoder {
    /**
     * 声明变量可复用该解码类
     */
    private Class<?> clazz;
    public RpcDecoder(Class<?> clazz){
        this.clazz = clazz;
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        log.error("开始解码");
        //首先判断是否有一个整型数值得长度
        if(in.readableBytes() < 4){
            return;
        }
        //标记一下readIndex
        in.markReaderIndex();
        //获取数据长度
        int length = in.readInt();
        if(length <= 0){
            //说明没有数据传输，关闭通道
            ctx.close();
        }
        if(in.readableBytes() < length){
            //说明此时还没有读取到全部得invoker
            in.resetReaderIndex();
            return;
        }
        byte[] invoker = new byte[length];
        in.readBytes(invoker);
        log.error("invoker:{}",new String(invoker,"utf-8"));
        //反序列化字节流
        Object o = SerializeUtil.deserialize(invoker,clazz);
        out.add(o);
    }
}
