/**
 * 
 */
package com.googlecode.jobop.bootstrap.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * 自定义注解的解析器
 * 
 * @author van
 * 
 */
public class JobopNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		// 解析jobop:service標籤
		registerBeanDefinitionParser("service", new ProviderBeanDefinitionParser());
		// 解析jobop:reference標籤
		registerBeanDefinitionParser("reference", new ConsumerBeanDefinitionParser());
	}

}
