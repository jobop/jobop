package com.googlecode.jobop.bootstrap;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import com.googlecode.jobop.common.utils.DclSingletonUtil;
import com.googlecode.jobop.connector.component.BootstrapComponent;
import com.googlecode.jobop.core.supportors.JobopConsumerSubcribeSupport;
import com.googlecode.jobop.core.supportors.JobopProviderPublishSupport;
import com.googlecode.jobop.core.supportors.JobopProviderRegistrySupport;

public class JobopComponentBootstrap implements JobopBootstrap {
	/** 需要启动的所有组件 */

	/** 注册服务提供者支撑 */
	private JobopProviderRegistrySupport providerRegistrySupport = DclSingletonUtil.getSingleton(JobopProviderRegistrySupport.class);
	/** 发布服务提供者支撑 */
	private JobopProviderPublishSupport providerPublishSupport = DclSingletonUtil.getSingleton(JobopProviderPublishSupport.class);
	private JobopConsumerSubcribeSupport jobopConsumerSubcribeSupport = null;

	protected Set<BootstrapComponent> bootstrapComponents = new HashSet<BootstrapComponent>();
	private AtomicLong initAndtimeFlag = new AtomicLong(0);
	private AtomicLong startAndtimeFlag = new AtomicLong(0);
	private AtomicLong shutdownAndtimeFlag = new AtomicLong(0);

	@Override
	public void init() {
		if (initAndtimeFlag.compareAndSet(0, System.currentTimeMillis())) {
			System.out.println("唯一启动器，init。。");

			// 初始化所有组件
			for (JobopBootstrap bootstrap : bootstrapComponents) {
				bootstrap.init();
			}
			providerRegistrySupport.init();
			providerPublishSupport.init();
			jobopConsumerSubcribeSupport.init();
		}
	}

	@Override
	public void startup() {
		if (startAndtimeFlag.compareAndSet(0, System.currentTimeMillis())) {
			System.out.println("唯一启动器，startup。。");
			// 启动所有组件
			for (JobopBootstrap bootstrap : bootstrapComponents) {
				bootstrap.startup();
			}
			providerRegistrySupport.startup();
			providerPublishSupport.startup();
			jobopConsumerSubcribeSupport.startup();
		}
	}

	@Override
	public void shutdown() {
		if (shutdownAndtimeFlag.compareAndSet(0, System.currentTimeMillis())) {
			// 关闭所有组件
			for (JobopBootstrap bootstrap : bootstrapComponents) {
				try {
					bootstrap.shutdown();
					// FIXME:here remove may be ef the iterator
					removeComponent(bootstrap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			providerRegistrySupport.shutdown();
			providerPublishSupport.shutdown();
		}

	}

	/**
	 * 注册启动组件,spring don't use this
	 * 
	 * @param component
	 */
	public void registComponent(BootstrapComponent component) {
		synchronized (this) {
			bootstrapComponents.add(component);
		}
	}

	/**
	 * 去除組件
	 * 
	 * @param component
	 */
	public void removeComponent(JobopBootstrap component) {
		synchronized (this) {
			bootstrapComponents.remove(component);
		}
	}

	public Set<BootstrapComponent> getBootstrapComponents() {
		return bootstrapComponents;
	}

	public void setBootstrapComponents(Set<BootstrapComponent> bootstrapComponents) {
		this.bootstrapComponents = bootstrapComponents;
	}
}
