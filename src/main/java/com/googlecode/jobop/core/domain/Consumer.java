package com.googlecode.jobop.core.domain;

public class Consumer {
	private String appGroup;
	private String methodSign;
	/** appgroup+方法签名 */
	private String key;

	private Object[] methodArgus;

	public Object[] getMethodArgus() {
		return methodArgus;
	}

	public void setMethodArgus(Object[] methodArgus) {
		this.methodArgus = methodArgus;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAppGroup() {
		return appGroup;
	}

	public void setAppGroup(String appGroup) {
		this.appGroup = appGroup;
	}

	public String getMethodSign() {
		return methodSign;
	}

	public void setMethodSign(String methodSign) {
		this.methodSign = methodSign;
	}

}
