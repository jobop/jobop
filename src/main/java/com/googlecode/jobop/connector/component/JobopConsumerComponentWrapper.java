package com.googlecode.jobop.connector.component;

import org.springframework.beans.factory.FactoryBean;

/**
 * 包装JobopConsumerComponet，屏蔽核心跟spring的依赖
 * 
 * @author van
 * 
 */
public class JobopConsumerComponentWrapper implements FactoryBean{
	private JobopConsumerComponent jobopConsumerComponet;

	@Override
	public Object getObject() throws Exception {
		return jobopConsumerComponet.getObject();
	}

	@Override
	public Class<? extends Object> getObjectType() {
		return jobopConsumerComponet.getObjectType();
	}

	@Override
	public boolean isSingleton() {
		return jobopConsumerComponet.isSingleton();
	}

	public JobopConsumerComponent getJobopConsumerComponet() {
		return jobopConsumerComponet;
	}

	public void setJobopConsumerComponet(JobopConsumerComponent jobopConsumerComponet) {
		this.jobopConsumerComponet = jobopConsumerComponet;
	}

}
