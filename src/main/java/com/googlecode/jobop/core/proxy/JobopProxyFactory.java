package com.googlecode.jobop.core.proxy;

import com.googlecode.jobop.common.bean.Destroyable;

/**
 * 代理工廠接口
 * 
 * @author van
 * 
 */
public interface JobopProxyFactory extends Destroyable {
	/**
	 * 獲取一個代理
	 * 
	 * @return
	 */
	public <T> T generateProxy(String interfaceName, String readTimeout, String appGroup, Object refObj);

}
