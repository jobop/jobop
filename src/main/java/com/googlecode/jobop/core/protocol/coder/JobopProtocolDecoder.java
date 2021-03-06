package com.googlecode.jobop.core.protocol.coder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.googlecode.jobop.core.protocol.JobopProtocol;

/**
 * Jobop协议层的解码器,把channelbuffer转化成一个JobopProtocol对象
 * 
 * @author van
 * 
 */
public class JobopProtocolDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer cb) throws Exception {
		// 前面4个字节表示数据长度，如果可读字节不够的话，表示数据不完整直接返回null，等待下次数据
		if (cb.readableBytes() < 4) {
			return null;
		}
		// 数据长度数据足够，需要标记一下开始位置，因为可能报文体长度不足，读到一般需要挂起，需要重新从标志位开始读
		cb.markReaderIndex();
		// 读取数据长度
		int length = cb.readInt();
		if (cb.readableBytes() < length) {
			// 剩下的可读长度不够，需要复位开始位置，然后挂起
			cb.resetReaderIndex();
			return null;
		}
		// 如果长度也足够，则填充JobopProtocol对象
		JobopProtocol protocol = new JobopProtocol();
		protocol.setDataLenth(length);
		byte[] bytes = new byte[length];
		cb.readBytes(bytes);
		protocol.setDatas(bytes);
		return protocol;
	}

}
