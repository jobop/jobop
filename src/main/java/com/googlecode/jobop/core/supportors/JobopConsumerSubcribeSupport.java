package com.googlecode.jobop.core.supportors;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.util.CollectionUtils;

import com.googlecode.jobop.bootstrap.JobopBootstrap;
import com.googlecode.jobop.core.domain.Consumer;
import com.googlecode.jobop.core.holder.ConsumerHolder;
import com.googlecode.jobop.core.holder.RouteHolder;
import com.googlecode.jobop.core.holder.ZkHolder;
import com.googlecode.jobop.core.policy.Route;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.api.BackgroundCallback;
import com.netflix.curator.framework.api.CuratorEvent;
import com.netflix.curator.framework.api.CuratorEventType;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;

public class JobopConsumerSubcribeSupport implements JobopBootstrap {
	@SuppressWarnings("unused")
	private static JobopConsumerSubcribeSupport jobopConsumerSubcribeSupport;
	/**
	 * 启动标识，用来记录开始时间和启动唯一性
	 */
	private AtomicLong startAndtimeFlag = new AtomicLong(0);

	private JobopConsumerSubcribeSupport() {
		System.out.println("1111111111111111111");
	}

	@Override
	public void init() {
		ZkHolder.getInstance().getZkclient();

	}

	@Override
	public void startup() {
		// 此代码只执行一次
		if (startAndtimeFlag.compareAndSet(0, System.currentTimeMillis())) {
			if (ZkHolder.getInstance().isConnected()) {
				doSubcribe();
			} else {
				// 注册成为Jobop通知监听器，监听注册行为,未免重复通知，保证只有一个监听器注册
				ZkHolder.getInstance().getZkclient().getConnectionStateListenable().addListener(new ConnectionStateListener() {
					/**
					 * 监听连接事件和重连事件(连接事件过来后，要怎么知道服务已经全部可以注册？？)
					 */
					@Override
					public void stateChanged(CuratorFramework client, ConnectionState state) {
						if (state == ConnectionState.RECONNECTED) {
							ZkHolder.getInstance().setConnected(true);
							doSubcribe();
						}
					}
				});
				// start
				ZkHolder.getInstance().getZkclient().start();
			}
		}
	}

	private void doSubcribe() {

		for (final Entry<String, Consumer> entry : ConsumerHolder.getInstance().getConsumers().entrySet()) {
			final Consumer consumer = entry.getValue();
			final String path = String.format("/jobop/providers/%s/%s", consumer.getAppGroup(), consumer.getMethodSign());
			// do regiest the provider to zk
			try {
				List<String> addresses = ZkHolder.getInstance().getZkclient().getChildren().watched().inBackground(new BackgroundCallback() {

					@Override
					public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
						if (event.getType().equals(CuratorEventType.CHILDREN)) {
							List<String> addresses = event.getChildren();
							refreshRoute(consumer.getKey(), addresses);
						}
					}
				}).forPath(path);

				refreshRoute(consumer.getKey(), addresses);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// refreash the route for the consumer
	private void refreshRoute(String key, List<String> addresses) {
		Set<Route> routeSet = new HashSet<Route>();
		if (!CollectionUtils.isEmpty(addresses)) {
			for (String address : addresses) {
				Route route = new Route();
				route.setIp(address.split(":")[0]);
				route.setPort(Integer.valueOf(address.split(":")[1]));
				routeSet.add(route);
			}
		}
		RouteHolder.addRoutes(key, routeSet);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

}
