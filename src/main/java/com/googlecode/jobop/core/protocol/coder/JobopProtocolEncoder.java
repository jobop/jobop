package com.googlecode.jobop.core.protocol.coder;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.googlecode.jobop.core.protocol.JobopProtocol;
import com.googlecode.jobop.core.protocol.serializer.Serializer;
import com.googlecode.jobop.core.protocol.serializer.SerializerFactory;

/**
 * Jobop协议层编码器 在下层的编码器已经组装成protocol对象，这里把protocol对象转换成byte
 * 
 * @author van
 * 
 */
public class JobopProtocolEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		JobopProtocol protocol = (JobopProtocol) msg;
		Serializer serializer = SerializerFactory.getSerializer(SerializerFactory.HESSIAN_SERIALIZER);
		byte[] datas = serializer.encode(protocol);
		return datas;
	}

}
