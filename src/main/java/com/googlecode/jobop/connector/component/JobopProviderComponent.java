package com.googlecode.jobop.connector.component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.StringUtils;

import com.googlecode.jobop.common.utils.JobopSignUtil;
import com.googlecode.jobop.core.domain.Provider;
import com.googlecode.jobop.core.holder.ProvidersHolder;
import com.googlecode.jobop.core.proxy.JobopProxyFactory;
import com.googlecode.jobop.core.proxy.ProviderProxyFactory;

/**
 * 服务提供者组件，定义了服务提供者的启动行为
 * 
 * @author van
 * 
 */
public class JobopProviderComponent implements BootstrapComponent {

	private JobopProxyFactory factory = null;
	/** 接口名 */
	private String interfaceName;
	/** 鏈接超時 */
	private String connectionTimeout;
	/** 读取超时 */
	private String readTimeout;
	/** 应用组 */
	private String appGroup;
	/** 代理对象 */
	private Object proxyObj;
	/** 原生對象 */
	private Object refObj;

	/**
	 * 做一些創建鏈接之類的工作，包括創建任務處理線程，創建到zk的鏈接，啓動本地netty服務器,生成代理類
	 */
	@Override
	public void init() {
		initProvider();
		factory = ProviderProxyFactory.newInstance();
		proxyObj = factory.generateProxy(interfaceName, readTimeout, appGroup, refObj);
	}

	/**
	 * 做一些服务器启动的工作，包括把服务在注册到任务worker中，到zk注册该服务等
	 */
	@Override
	public void startup() {
	}

	/**
	 * 发布服务
	 * 
	 * @throws ClassNotFoundException
	 */
	private void initProvider() {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (null == clazz) {
			return;
		}
		if (!(clazz.isAssignableFrom(refObj.getClass()))) {
			// TODO:日志
			return;
		}
		Method[] methods = clazz.getDeclaredMethods();
		if (null == methods || methods.length == 0) {
			// TODO:日志
			return;
		}
		for (Method method : methods) {
			// 只发布公共方法
			if (!Modifier.isPublic(method.getModifiers())) {
				// TODO:日志
				return;
			}
			String methodSign = JobopSignUtil.signature(method);
			Provider provider = assembleProvider(clazz, methodSign);
			ProvidersHolder.getInstance().addHandler(methodSign, provider);

		}
	}

	/**
	 * 组装provider对象
	 * 
	 * @param clazz
	 * @param methodSign
	 * @return
	 */
	private Provider assembleProvider(Class<?> clazz, String methodSign) {
		Provider provider = new Provider();
		provider.setAppGroup(this.appGroup);
		provider.setConnectionTimeout(Long.parseLong(StringUtils.isEmpty(this.connectionTimeout) ? "5000" : this.connectionTimeout));
		provider.setProxyObj(this.proxyObj);
		provider.setReadTimeout(Long.parseLong(StringUtils.isEmpty(this.readTimeout) ? "5000" : this.readTimeout));
		provider.setInterfaceClazz(clazz);

		provider.setMethodSign(methodSign);
		provider.setKey(this.appGroup + methodSign);
		return provider;
	}

	@Override
	public void shutdown() {
		factory.destroy();
	}

	public JobopProxyFactory getFactory() {
		return factory;
	}

	public void setFactory(JobopProxyFactory factory) {
		this.factory = factory;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(String connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(String readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getAppGroup() {
		return appGroup;
	}

	public void setAppGroup(String appGroup) {
		this.appGroup = appGroup;
	}

	public Object getProxyObj() {
		return proxyObj;
	}

	public void setProxyObj(Object proxyObj) {
		this.proxyObj = proxyObj;
	}

	public Object getRefObj() {
		return refObj;
	}

	public void setRefObj(Object refObj) {
		this.refObj = refObj;
	}

}
