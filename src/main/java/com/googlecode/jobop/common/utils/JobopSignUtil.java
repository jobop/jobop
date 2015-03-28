package com.googlecode.jobop.common.utils;

import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.Maps;

public class JobopSignUtil {
	/*
	 * 方法签名缓存
	 */
	private static Map<Method, String> methodSignCache = Maps.newConcurrentMap();

	/**
	 * 获取当前方法的服务签名
	 * 
	 * @param method
	 * @return
	 */
	public static String signature(Method method) {

		if (methodSignCache.containsKey(method)) {
			return methodSignCache.get(method);
		}

		final StringBuilder methodStr = new StringBuilder();
		methodStr.append(method.getReturnType().getName()).append(method.getDeclaringClass().getName())
				.append(method.getName());
		final Class<?>[] paramTypes = method.getParameterTypes();
		if (null != paramTypes && paramTypes.length != 0) {
			for (int i = 0; i < paramTypes.length; i++) {
				methodStr.append(paramTypes[i].getName());
			}
		}

		String signMD5 = MD5Util.digest(methodStr.toString());
		methodSignCache.put(method, signMD5);
		return signMD5;
	}

}
