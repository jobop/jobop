package com.googlecode.jobop.connector.component;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jobop.common.bean.FactoryBeanable;
import com.googlecode.jobop.common.utils.JobopSignUtil;
import com.googlecode.jobop.core.domain.Consumer;
import com.googlecode.jobop.core.holder.ConsumerHolder;
import com.googlecode.jobop.core.proxy.ConsumerProxyFactory;

/**
 * 服务消费者组件，定义了服务消费者的启动行为
 * 
 * @author van
 * 
 */
public class JobopConsumerComponent implements BootstrapComponent, FactoryBeanable {
	private static Logger logger = LoggerFactory.getLogger(JobopConsumerComponent.class);
	/** 接口名 */
	private String interfaceName;
	/** 鏈接超時 */
	private String connectionTimeout;
	/** 读取超时 */
	private String readTimeout;
	/** 应用组 */
	private String appGroup;
	/** 轮寻规则 */
	private String policy;
	private ConsumerProxyFactory consumerProxyFactory = new ConsumerProxyFactory();

	@Override
	public void init() {
		logger.info("服务消費者组件init。。。" + "interfaceName=" + interfaceName + " connectionTimeout=" + connectionTimeout + " readTimeout=" + readTimeout + " appGroup=" + appGroup + " policy=" + policy);
		initConsumer();
	}

	@Override
	public void startup() {
		logger.info("服务消費者组件startup。。。" + "interfaceName=" + interfaceName + " connectionTimeout=" + connectionTimeout + " readTimeout=" + readTimeout + " appGroup=" + appGroup + " policy=" + policy);
	}

	@Override
	public void shutdown() {

	}

	@Override
	public Object getObject() throws Exception {
		logger.info("服务消費者组件getobject。。。" + "interfaceName=" + interfaceName + " connectionTimeout=" + connectionTimeout + " readTimeout=" + readTimeout + " appGroup=" + appGroup + " policy=" + policy);
		return consumerProxyFactory.generateProxy(interfaceName, readTimeout, appGroup, null);
	}

	@Override
	public Class<? extends Object> getObjectType() {
		try {
			return Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return Object.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	private void initConsumer() {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (null == clazz) {
			return;
		}
		if (!clazz.getName().equals(interfaceName)) {
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
			Consumer consumer = assembleConsumer(clazz, methodSign);
			ConsumerHolder.getInstance().addConsumer(methodSign, consumer);

		}
	}

	private Consumer assembleConsumer(Class<?> clazz, String methodSign) {
		Consumer consumer = new Consumer();
		consumer.setKey(this.appGroup + methodSign);
		consumer.setAppGroup(this.appGroup);
		consumer.setMethodSign(methodSign);
		return consumer;
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

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

}
