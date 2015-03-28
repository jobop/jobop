package com.googlecode.jobop.core.policy;

import java.util.HashMap;
import java.util.Map;

public class PolicyFactory {
	private static Map<String, Policy> policyMap = new HashMap<String, Policy>();

	public static Policy getPolicy(String name) {
		return policyMap.get(name);
	}
}
