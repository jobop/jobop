package com.googlecode.jobop.core.protocol.handlers;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * 服务提供在处理请求的handler
 * 
 * @author van
 * 
 */
public class JobopProviderHandler extends SimpleChannelUpstreamHandler {
	private JobopRequestProcessor processor;

	public JobopProviderHandler() {
	}

	public JobopProviderHandler(JobopRequestProcessor processor) {
		this.processor = processor;
	}

	/**
	 * Invoked when a message object (e.g: {@link ChannelBuffer}) was received
	 * from a remote peer.
	 */
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		// processor.doProcess(ctx, e);
		ctx.sendUpstream(e);
	}

	/**
	 * Invoked when an exception was raised by an I/O thread or a
	 * {@link ChannelHandler}.
	 */
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		if (this == ctx.getPipeline().getLast()) {
		}
		ctx.sendUpstream(e);
	}
}
