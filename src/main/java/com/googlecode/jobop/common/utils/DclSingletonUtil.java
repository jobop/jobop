package com.googlecode.jobop.common.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * double check lock util?
 * 
 * @project jobop
 * @author van
 * @date 2015-3-18 Copyright (C) 2010-2012 www.2caipiao.com Inc. All rights
 *       reserved.
 */
public class DclSingletonUtil {
	private static Map<Class<?>, Field> singletonFieldCache = new ConcurrentHashMap<Class<?>, Field>();

	public static <T> T getSingleton(final Class<T> clazz) {
		if (null == clazz) {
			// FIXME:throw ex?
			return null;
		}
		Field objField = singletonFieldCache.get(clazz);
		if (null == objField) {
			for (Field f : clazz.getDeclaredFields()) {
				if (f.getType().equals(clazz)) {
					objField = f;
					singletonFieldCache.put(clazz, objField);
					break;
				}
			}
		}
		if (null == objField) {
			// FIXME:throw ex?
			return null;
		}
		Object obj = null;
		try {
			objField.setAccessible(true);
			obj = objField.get(clazz);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		if (obj == null) {
			synchronized (clazz) {
				if (obj == null) {
					try {
						Constructor<?> ct = clazz.getDeclaredConstructor(new Class[] {});
						ct.setAccessible(true);
						obj = ct.newInstance(new Object[] {});
						objField.set(obj, obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (T) obj;
	}
}
