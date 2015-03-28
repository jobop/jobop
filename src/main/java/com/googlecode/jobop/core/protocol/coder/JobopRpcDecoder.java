package com.googlecode.jobop.core.protocol.coder;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.googlecode.jobop.core.protocol.JobopProtocol;
import com.googlecode.jobop.core.protocol.serializer.Serializer;
import com.googlecode.jobop.core.protocol.serializer.SerializerFactory;

/**
 * Jobop链路层解码器,接收上层的消息，组装成真实对象
 * 
 * @author van
 * 
 */
public class JobopRpcDecoder extends OneToOneDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		// 上层已经解码为Protocol对象，这次把其中的byte数组再次解码
		JobopProtocol protocol = (JobopProtocol) msg;
		byte[] dataByte = protocol.getDatas();
		if (dataByte.length != protocol.getDataLenth()) {
			// TODO：出错，抛异常
		}
		// 获取序列化工具
		Serializer serializer = SerializerFactory.getSerializer(SerializerFactory.HESSIAN_SERIALIZER);
		return serializer.decode(dataByte);
	}

}
