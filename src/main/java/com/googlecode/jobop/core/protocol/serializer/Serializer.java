package com.googlecode.jobop.core.protocol.serializer;

import java.io.Serializable;

/**
 * 序列化工具接口
 * 
 * @author van
 * 
 */
public interface Serializer {
	/**
	 * 序列化
	 * 
	 * @param object
	 * @return
	 */
	public <T extends Serializable> byte[] encode(T object);

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public <T extends Serializable> T decode(byte[] bytes);
}
