package com.googlecode.jobop.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 服务提供者的代理生成工厂
 * 
 * @author van
 * 
 */
public class ProviderProxyFactory implements JobopProxyFactory {

	private static JobopProxyFactory factory = null;
	private static Lock lock = new ReentrantLock();

	private ProviderProxyFactory() {
	}

	public static JobopProxyFactory newInstance() {
		if (null == factory) {
			try {
				lock.lock();
				if (factory == null) {
					factory = new ProviderProxyFactory();
				}
			} finally {
				lock.unlock();
			}
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object generateProxy(String interfaceName, String readTimeout, String appGroup, final Object refObj) {
		// proxy对象考虑用JMX监控，并控制其开关
		return Proxy.newProxyInstance(refObj.getClass().getClassLoader(), refObj.getClass().getInterfaces(),
				new InvocationHandler() {

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						return method.invoke(refObj, args);
					}
				});
	}

	@Override
	public void destroy() {

	}

}
