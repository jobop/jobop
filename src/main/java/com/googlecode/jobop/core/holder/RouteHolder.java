package com.googlecode.jobop.core.holder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.googlecode.jobop.core.policy.Route;

public class RouteHolder {
	private static ReadWriteLock lock = new ReentrantReadWriteLock();
	private static Lock readLock = lock.readLock();
	private static Lock writeLock = lock.writeLock();
	public static Map<String, Set<Route>> routeMap = new HashMap<String, Set<Route>>();

	public static void addRoutes(String key, Set<Route> routes) {
		try {
			writeLock.lock();
			routeMap.put(key, routes);
		} finally {
			writeLock.unlock();
		}
	}

	public static Set<Route> getRoutes(String key) {
		try {
			readLock.lock();
			return routeMap.get(key);
		} finally {
			readLock.unlock();
		}
	}
}
