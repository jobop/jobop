package com.googlecode.jobop.connector.exchange;

import java.io.Serializable;

public class JobopResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Object resultObj;
	private int id;

	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
