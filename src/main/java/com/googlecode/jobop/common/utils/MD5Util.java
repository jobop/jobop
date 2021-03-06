/**
 * 
 */
package com.googlecode.jobop.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author van
 * 
 */
public class MD5Util {

	/**
	 * MD5标准计算摘要
	 * */
	public static String digest(String inbuf) {
		MessageDigest m;
		try {
			m = MessageDigest.getInstance("MD5");
			m.update(inbuf.getBytes(), 0, inbuf.length());
			return new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			// TODO:日志
			e.printStackTrace();
		}
		return null;
	}

}
