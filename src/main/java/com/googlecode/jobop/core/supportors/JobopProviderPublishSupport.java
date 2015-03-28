package com.googlecode.jobop.core.supportors;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.util.CollectionUtils;

import com.googlecode.jobop.bootstrap.JobopBootstrap;
import com.googlecode.jobop.common.utils.DclSingletonUtil;
import com.googlecode.jobop.common.utils.IpUtil;
import com.googlecode.jobop.connector.exchange.JobopRequest;
import com.googlecode.jobop.core.protocol.coder.JobopProtocolDecoder;
import com.googlecode.jobop.core.protocol.coder.JobopProtocolEncoder;
import com.googlecode.jobop.core.protocol.coder.JobopRpcDecoder;
import com.googlecode.jobop.core.protocol.coder.JobopRpcEncoder;
import com.googlecode.jobop.core.protocol.handlers.JobopRequestProcessor;

/**
 * 发布服务的支撑
 * 
 * @author van
 * 
 */
public class JobopProviderPublishSupport implements JobopBootstrap, ChannelPipelineFactory {
	/**
	 * 启动标识，用来记录开始时间和启动唯一性
	 */
	private static AtomicLong startAndtimeFlag = new AtomicLong(0);
	private static AtomicLong endAndtimeFlag = new AtomicLong(0);
	private JobopRequestProcessor processor = DclSingletonUtil.getSingleton(JobopProviderProcessSupport.class);
	@SuppressWarnings("unused")
	private static JobopProviderPublishSupport jobopProviderPublishSupport = null;
	/**
	 * netty启动器
	 */
	private ServerBootstrap bootstrap;
	private ChannelGroup channelGroup;

	private JobopProviderPublishSupport() {
	}

	@Override
	public void init() {
		// 此代码只执行一次
		if (startAndtimeFlag.compareAndSet(0, System.currentTimeMillis())) {
			bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
			bootstrap.setPipelineFactory(this);
			bootstrap.setOption("child.tcpNoDelay", true);
			bootstrap.setOption("child.keepAlive", true);
			channelGroup = new DefaultChannelGroup();

		}
	}

	@Override
	public void startup() {
		// 发布端口写死吧，没啥好配置的
		channelGroup.add(bootstrap.bind(new InetSocketAddress(IpUtil.getIp(), 1986)));
	}

	@Override
	public void shutdown() {
		// 此代码只执行一次
		if (endAndtimeFlag.compareAndSet(0, System.currentTimeMillis())) {
			System.out.println("destory");
			if (!CollectionUtils.isEmpty(channelGroup)) {
				for (Channel channel : channelGroup) {
					channel.close();
				}
			}
			if (null != bootstrap) {
				bootstrap.releaseExternalResources();
			}
		}
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		// upstream接收消息，解码由顶向下，downstream发送消息，编码由底向上，注意拦截器注册顺序
		pipeline.addLast("jobop-protocol-decoder", new JobopProtocolDecoder());
		pipeline.addLast("jobop-rpc-decoder", new JobopRpcDecoder());

		pipeline.addLast("jobop-provider-handler", new SimpleChannelUpstreamHandler() {
			@Override
			public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
				super.channelConnected(ctx, e);
				channelGroup.add(ctx.getChannel());
			}

			public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
				JobopRequest request = (JobopRequest) e.getMessage();
				processor.doProcess(ctx, request);
				ctx.sendUpstream(e);
			}

			public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
				if (this == ctx.getPipeline().getLast()) {

				}
				ctx.sendUpstream(e);
			}
		});

		pipeline.addLast("jobop-protocol-encoder", new JobopProtocolEncoder());
		pipeline.addLast("jobop-rpc-encoder", new JobopRpcEncoder());
		return pipeline;
	}

}
