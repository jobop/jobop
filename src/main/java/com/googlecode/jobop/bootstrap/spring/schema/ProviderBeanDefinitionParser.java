package com.googlecode.jobop.bootstrap.spring.schema;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.googlecode.jobop.common.constants.JobopCommonConstant;
import com.googlecode.jobop.connector.component.JobopProviderComponent;

/**
 * 服務提供者組件解析器
 * 
 * @author van
 * 
 */
public class ProviderBeanDefinitionParser extends AbstractBeanDefinitionParser<JobopProviderComponent> {

	@Override
	protected BeanDefinition extraParse(BeanDefinition curDefinition, Element element, ParserContext parserContext) {
		// TODO:需要先檢查一下element中的元素是否在JobopProviderComponet中都有field或者getter和setter方法

		String appGroup = element.getAttribute(JobopCommonConstant.APP_GROUP);
		String interfaceName = element.getAttribute(JobopCommonConstant.INTERFACE_NAME);
		String connectionTimeout = element.getAttribute(JobopCommonConstant.CONNECTION_TIMEOUT);
		String readTimeout = element.getAttribute(JobopCommonConstant.READ_TIMEOUT);
		String refObjName = element.getAttribute(JobopCommonConstant.REF_OBJ);

		curDefinition.getPropertyValues().addPropertyValue("appGroup", appGroup);
		curDefinition.getPropertyValues().addPropertyValue("interfaceName", interfaceName);
		curDefinition.getPropertyValues().addPropertyValue("connectionTimeout", connectionTimeout);
		curDefinition.getPropertyValues().addPropertyValue("readTimeout", readTimeout);
		curDefinition.getPropertyValues().addPropertyValue("refObj", new RuntimeBeanReference(refObjName));

		return curDefinition;
	}

	@Override
	protected Class<JobopProviderComponent> getBeanClazz() {
		return JobopProviderComponent.class;
	}

	@Override
	protected String beanPrefix() {
		return "";
	}

}
