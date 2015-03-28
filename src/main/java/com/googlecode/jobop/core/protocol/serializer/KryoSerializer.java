package com.googlecode.jobop.core.protocol.serializer;

import java.io.Serializable;

/**
 * Kryo序列化工具
 * 
 * @author van
 * 
 */
public class KryoSerializer implements Serializer {

	@Override
	public <T extends Serializable> byte[] encode(T object) {
		return null;
	}

	@Override
	public <T extends Serializable> T decode(byte[] bytes) {
		return null;
	}

}
