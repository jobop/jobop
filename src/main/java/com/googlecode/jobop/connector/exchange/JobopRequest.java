package com.googlecode.jobop.connector.exchange;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class JobopRequest implements Serializable {
	/** 需要请求调用的服务提供者 */
	private Object content;
	private static final long serialVersionUID = 1L;
	private static AtomicInteger IDGENERATER = new AtomicInteger(0);
	private int id;
	private long readTimeout;

	public JobopRequest() {
		id = IDGENERATER.getAndIncrement();
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public long getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

}
