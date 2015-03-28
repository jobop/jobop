package com.googlecode.jobop.common.bean;

/**
 * 使该bean拥有类似FactoryBean的功能，重写spring的FactoryBean接口，用于防止强依赖spring，使日后扩展更为方便
 * 
 * @author van
 * 
 */
public interface FactoryBeanable {
	Object getObject() throws Exception;

	Class<?> getObjectType();

	boolean isSingleton();

}
