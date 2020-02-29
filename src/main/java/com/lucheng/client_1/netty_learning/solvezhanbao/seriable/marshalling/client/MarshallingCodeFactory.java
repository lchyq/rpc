package com.lucheng.client_1.netty_learning.solvezhanbao.seriable.marshalling.client;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * 基于jboss的 marshalling 编解码
 */
public class MarshallingCodeFactory {
    public static MarshallingDecoder buildMarshallingDecoder(){
        //获取java的编解码工厂
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        UnmarshallerProvider unmarshallerProvider = new DefaultUnmarshallerProvider(marshallerFactory,marshallingConfiguration);
        return new MarshallingDecoder(unmarshallerProvider,1024);
    }
    public static MarshallingEncoder buildMarshallingEncoder(){
        //获取java的编解码工厂
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        MarshallerProvider marshallerProvider = new DefaultMarshallerProvider(marshallerFactory,marshallingConfiguration);
        return new MarshallingEncoder(marshallerProvider);
    }
}
