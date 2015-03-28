package com.googlecode.jobop.core.holder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.jobop.core.domain.Provider;

public class ProvidersHolder {
	/**
	 * 需要注册的服务提供者
	 */
	private Map<String, Provider> providers = null;
	private static ProvidersHolder providersHolder = null;

	private ProvidersHolder() {
		providers = new ConcurrentHashMap<String, Provider>();
	}

	public static ProvidersHolder getInstance() {
		if (null == providersHolder) {
			synchronized (ProvidersHolder.class) {
				if (null == providersHolder) {
					providersHolder = new ProvidersHolder();
				}
			}
		}
		return providersHolder;
	}

	public Provider getHandler(String handlerKey) {
		return providers.get(handlerKey);
	}

	public void addHandler(String handlerKey, Provider handler) {
		providers.put(handlerKey, handler);
	}

	public Map<String, Provider> getProviders() {
		return providers;
	}
}
