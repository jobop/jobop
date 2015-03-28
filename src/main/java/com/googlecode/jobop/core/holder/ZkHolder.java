package com.googlecode.jobop.core.holder;

import static com.netflix.curator.framework.CuratorFrameworkFactory.newClient;

import java.io.IOException;

import com.googlecode.jobop.common.utils.PropertyUtil;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.retry.ExponentialBackoffRetry;

/**
 * 管理zkClient，保证全局只有一个client在使用
 * 
 * @author van
 * 
 */
public class ZkHolder {
	/**
	 * zk客户端
	 */
	private CuratorFramework client;
	private static ZkHolder zkHolder = null;
	private volatile boolean connected = false;

	private ZkHolder() {
		try {
			client = newClient(PropertyUtil.getZkServerList(), PropertyUtil.getZkSessionTimeout(), PropertyUtil.getZkConnectTimeout(), new ExponentialBackoffRetry(500, 20));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ZkHolder getInstance() {
		if (null == zkHolder) {
			synchronized (ZkHolder.class) {
				if (null == zkHolder) {
					zkHolder = new ZkHolder();
				}
			}
		}
		return zkHolder;

	}

	public CuratorFramework getZkclient() {
		return this.client;
	}

	public void clear() {
		client.close();
		client = null;
		zkHolder = null;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

}
