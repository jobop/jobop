package com.googlecode.jobop.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;

/**
 * 获取自身ip的工具
 * 
 * @author van
 * 
 */
public class IpUtil {
	private static String hostIp = StringUtils.EMPTY;;
	static {
		try {
			final Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					final String host = ips.nextElement().getHostAddress();
					if (!host.matches("^127\\..*") && host.matches("([0-9]{0,3}\\.){3}[0-9]{0,3}")) {
						hostIp = host;
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO:日志
		}

	}

	public static String getIp() {
		return hostIp;
	}

}
