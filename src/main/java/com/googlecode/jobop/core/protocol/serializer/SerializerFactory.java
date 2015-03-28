package com.googlecode.jobop.core.protocol.serializer;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * 序列化工具工厂
 * 
 * @author van
 * 
 */
public class SerializerFactory {
	private static Map<String, Serializer> serializers = Maps.newHashMap();
	public static String HESSIAN_SERIALIZER = "hessian_serializer";
	public static String KRYO_SERIALIZER = "kryo_serializer";
	static {
		serializers.put(HESSIAN_SERIALIZER, new HessianSerializer());
		serializers.put(KRYO_SERIALIZER, new KryoSerializer());
	}

	public static Serializer getSerializer(String serializerName) {
		return serializers.get(serializerName);
	}
}
