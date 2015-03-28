package com.googlecode.jobop.core.protocol;

import java.io.Serializable;

/**
 * jobop协议对象
 * 
 * @author van
 * 
 */
public class JobopProtocol implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 数据长度 */
	private int dataLenth;
	/** 数据 */
	private byte[] datas;

	public int getDataLenth() {
		return dataLenth;
	}

	public void setDataLenth(int dataLenth) {
		this.dataLenth = dataLenth;
	}

	public byte[] getDatas() {
		return datas;
	}

	public void setDatas(byte[] datas) {
		this.datas = datas;
	}

}
