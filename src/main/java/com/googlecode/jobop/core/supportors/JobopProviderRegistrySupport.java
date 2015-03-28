package com.googlecode.jobop.core.supportors;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jobop.bootstrap.JobopBootstrap;
import com.googlecode.jobop.common.utils.IpUtil;
import com.googlecode.jobop.core.domain.Provider;
import com.googlecode.jobop.core.holder.BootstrapAssistThreadHolder;
import com.googlecode.jobop.core.holder.ProvidersHolder;
import com.googlecode.jobop.core.holder.ZkHolder;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;

/**
 * jobop服务提供者註冊支撐
 * 
 * @author van
 * 
 */
public class JobopProviderRegistrySupport implements JobopBootstrap {
	private static Logger logger = LoggerFactory.getLogger(JobopProviderRegistrySupport.class);
	@SuppressWarnings("unused")
	private static JobopProviderRegistrySupport jobopProviderRegistrySupport;
	/**
	 * 启动标识，用来记录开始时间和启动唯一性
	 */
	private AtomicLong startAndtimeFlag = new AtomicLong(0);

	private JobopProviderRegistrySupport() {
	}

	@Override
	public void init() {
		ZkHolder.getInstance().getZkclient();
	}

	@Override
	public void startup() {
		// 此代码只执行一次
		if (startAndtimeFlag.compareAndSet(0, System.currentTimeMillis())) {

			// 注册成为Jobop通知监听器，监听注册行为,未免重复通知，保证只有一个监听器注册
			if (ZkHolder.getInstance().isConnected()) {
				doRegist();
			} else {
				ZkHolder.getInstance().getZkclient().getConnectionStateListenable().addListener(new ConnectionStateListener() {
					@Override
					public void stateChanged(CuratorFramework client, ConnectionState state) {
						if (state == ConnectionState.RECONNECTED) {
							doRegist();
						}
					}

				});
				// start
				ZkHolder.getInstance().getZkclient().start();
			}
		}
	}

	private void doRegist() {
		for (final Entry<String, Provider> entry : ProvidersHolder.getInstance().getProviders().entrySet()) {
			registProvider(entry.getValue());
		}
	}

	@Override
	public void shutdown() {
		// 不为0的时候才关闭，并且设置为0,只执行一次
		if (!startAndtimeFlag.compareAndSet(0, 0)) {
			ZkHolder.getInstance().clear();
		}
	}

	/**
	 * 注册服务提供者
	 * 
	 * @param provider
	 */
	private void registProvider(final Provider provider) {
		final String path = String.format("/jobop/providers/%s/%s", provider.getAppGroup(), provider.getMethodSign());
		// 没有路径就创建
		try {
			if (ZkHolder.getInstance().getZkclient().checkExists().forPath(path) == null) {
				ZkHolder.getInstance().getZkclient().create().creatingParentsIfNeeded().forPath(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// 注册临时节点，随客户端断开就会销毁
			ZkHolder.getInstance().getZkclient().create().withMode(CreateMode.EPHEMERAL).inBackground().forPath(String.format("%s/%s", path, IpUtil.getIp() + ":1986"));
		} catch (Exception e) {
			logger.error("注册服务提供者失败", e);
		}
	}

}
