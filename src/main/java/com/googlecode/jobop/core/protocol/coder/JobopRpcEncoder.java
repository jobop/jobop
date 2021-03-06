package com.googlecode.jobop.core.protocol.coder;

import java.io.Serializable;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import com.googlecode.jobop.core.protocol.JobopProtocol;
import com.googlecode.jobop.core.protocol.serializer.Serializer;
import com.googlecode.jobop.core.protocol.serializer.SerializerFactory;

/**
 * Jobop链路层编码器 把需要传输的obj对象，序列化为字节流，填充到Protocol中
 * 
 * @author van
 * 
 */
public class JobopRpcEncoder extends OneToOneEncoder {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
		if (!(msg instanceof Serializable)) {
			// TODO:异常
		}
		Serializable serializableObj = (Serializable) msg;
		// 获取序列化工具
		Serializer serializer = SerializerFactory.getSerializer(SerializerFactory.HESSIAN_SERIALIZER);
		byte[] datas = serializer.encode(serializableObj);
		JobopProtocol protocol = new JobopProtocol();
		protocol.setDataLenth(datas.length);
		protocol.setDatas(datas);
		return protocol;
	}
}
