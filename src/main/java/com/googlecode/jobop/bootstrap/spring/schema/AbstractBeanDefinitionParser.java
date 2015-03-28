package com.googlecode.jobop.bootstrap.spring.schema;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.googlecode.jobop.bootstrap.SpringContextBootstrapAdpter;
import com.googlecode.jobop.common.constants.JobopCommonConstant;

/**
 * 抽象bean定义的解析器，提供Jobop组件注册和bean定义
 * 
 * @author van
 * 
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBeanDefinitionParser<T> implements BeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		//zhege fangfa xian zhuru bean
		BeanDefinition beandefinition = initAndRegistBean(element, parserContext);
		return extraParse(beandefinition, element, parserContext);
	}

	private BeanDefinition initAndRegistBean(Element element, ParserContext parserContext) {
		Class<T> beanClazz = this.getBeanClazz();
		// 获取组件bean的唯一标识
		String identify = element.getAttribute(JobopCommonConstant.ID);
		if (StringUtils.isEmpty(identify)) {
			identify = element.getAttribute(JobopCommonConstant.NAME);
		}
		if (StringUtils.isEmpty(identify)) {
			identify = element.getAttribute(JobopCommonConstant.APP_GROUP)
					+ element.getAttribute(JobopCommonConstant.INTERFACE_NAME);
		}
		if (StringUtils.isEmpty(identify)) {
			// TODO:定義異常
		}
		identify = beanPrefix() + identify;

		BeanDefinition beanDefinition = new RootBeanDefinition(beanClazz);
		// 注册bean
		parserContext.getRegistry().registerBeanDefinition(identify, beanDefinition);
		
		// 获取全局唯一的启动器
		RootBeanDefinition bootstrapBean = null;
		try {
			bootstrapBean = (RootBeanDefinition) parserContext.getRegistry().getBeanDefinition(
					JobopCommonConstant.BOOTSTARP_BEAN);
		} catch (Exception e) {
			if (!(e instanceof NoSuchBeanDefinitionException)) {
				// TODO：日志
			}
		}

		if (null == bootstrapBean) {
			// FIXME:這裏考慮是否需要加鎖
			bootstrapBean = new RootBeanDefinition(SpringContextBootstrapAdpter.class);
			// 设置组件set的值为一个可合并的manageSet
			MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
			PropertyValue propertyValue = new PropertyValue("bootstrapComponents", new ManagedSet());
			mutablePropertyValues.addPropertyValue(propertyValue);

			bootstrapBean.setPropertyValues(mutablePropertyValues);
			// 註冊唯一啓動器
			parserContext.getRegistry().registerBeanDefinition(JobopCommonConstant.BOOTSTARP_BEAN, bootstrapBean);

		}
		ManagedSet components = (ManagedSet) bootstrapBean.getPropertyValues().getPropertyValue("bootstrapComponents")
				.getValue();
		// 把啓動組件塞到全局啓動器裏面
		components.add(beanDefinition);
		return beanDefinition;
	}

	/**
	 * 額外的bean解析，需要子類實現
	 * 
	 * @return
	 */
	protected abstract BeanDefinition extraParse(BeanDefinition curDefinition, Element element,
			ParserContext parserContext);

	/**
	 * 获取要解析的bean类型
	 * 
	 * @return
	 */
	protected abstract Class<T> getBeanClazz();

	/**
	 * 注册bean的前缀,有些bean需要包装，通过id取的时候需要取得包装后的bean，这里设置前缀是为了，不让真实bean占用其id
	 * 
	 * @return
	 */
	protected abstract String beanPrefix();
}
