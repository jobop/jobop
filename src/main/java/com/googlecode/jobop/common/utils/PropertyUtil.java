package com.googlecode.jobop.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取属性信息
 * 
 * @author van
 * 
 */
public final class PropertyUtil {

	private static final String PROPERTY_CLASSPATH = "/jobop.properties";

	private static Properties properties = new Properties();

	public static final String ZK_CONNECT_TIMEOUT = "jobop.zookeeper.connectTimeout";

	public static final String ZK_SESSION_TIMEOUT = "jobop.zookeeper.sessionTimeout";

	public static final String ZK_SERVER_LIST = "jobop.zookeeper.servers";

	private static String zkServerList = "";
	private static int zkSessionTimeout = 5000;
	private static int zkConnectTimeout = 5000;

	static {

		InputStream is = null;
		try {
			is = PropertyUtil.class.getResourceAsStream(PROPERTY_CLASSPATH);
			if (null == is) {
				throw new IllegalStateException(
						"jobop.properties can not found in the classpath.");
			}
			properties.load(is);
			preloadZkInfo();

		} catch (Throwable t) {
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}

	}

	private static void preloadZkInfo() {
		zkServerList = properties.getProperty(ZK_SERVER_LIST);
		zkSessionTimeout = Integer.parseInt(properties
				.getProperty(ZK_SESSION_TIMEOUT));
		zkConnectTimeout = Integer.parseInt(properties
				.getProperty(ZK_CONNECT_TIMEOUT));
	}

	public static String getZkServerList() {
		return zkServerList;
	}

	public static int getZkSessionTimeout() {
		return zkSessionTimeout;
	}

	public static int getZkConnectTimeout() {
		return zkConnectTimeout;
	}

}
