package com.lucheng.mydubbo_3.serialization;

import com.lucheng.mydubbo_3.Util.SerializeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * rpc请求编码类
 * @author lucheng28
 * @date 2020-03-14
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder<Serializable> {
    /**
     * @param ctx handler
     * @param msg 需要编码得数据
     * @param out 字节缓冲区
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        log.error("开始编码 msg:{}",msg);
        //序列化为字节数组
            byte[] bytes = SerializeUtil.serialize(msg);
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
    }
}
