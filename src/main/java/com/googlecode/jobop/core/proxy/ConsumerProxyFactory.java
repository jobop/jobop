package com.googlecode.jobop.core.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.googlecode.jobop.common.utils.JobopSignUtil;
import com.googlecode.jobop.core.policy.PolicyFactory;
import com.googlecode.jobop.core.policy.Route;

/**
 * 服务消費者的代理生成工厂
 * 
 * @author van
 * 
 */
public class ConsumerProxyFactory implements JobopProxyFactory {

	@Override
	public Object generateProxy(String interfaceName, String readTimeout, final String appGroup, Object refObj) {
		// proxy对象考虑用JMX监控，并控制其开关
		return Proxy.newProxyInstance(refObj.getClass().getClassLoader(), refObj.getClass().getInterfaces(), new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

				String methodSign = JobopSignUtil.signature(method);
				Route route = PolicyFactory.getPolicy("asdf").chooseRoute(appGroup + methodSign);

				// here to send msg to route then get the result

				return null;
			}
		});
	}

	@Override
	public void destroy() {

	}

}
