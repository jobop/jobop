package com.googlecode.jobop.bootstrap;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * 基于spring上下文的启动器桥接，用于链接spring上下文和jobop启动方式.
 * 
 * @author van
 * 
 */
public class SpringContextBootstrapAdpter extends JobopComponentBootstrap implements ApplicationListener {

	/**
	 * 在上下文加载完毕侯，开始启动服务
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextStartedEvent) {
			this.init();
			this.startup();
		} else if (event instanceof ContextStoppedEvent || event instanceof ContextClosedEvent) {
			this.shutdown();
		}
	}
}
