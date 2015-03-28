package com.googlecode.jobop.core.protocol.handlers;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;

import com.googlecode.jobop.connector.exchange.JobopRequest;

/**
 * jobop的请求处理工具
 * 
 * @author van
 * 
 */
public interface JobopRequestProcessor {
	/**
	 * 处理请求
	 * 
	 * @param ctx
	 * @param e
	 */
	public void doProcess(ChannelHandlerContext ctx, JobopRequest request);
}
