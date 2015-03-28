package com.googlecode.jobop.connector.exchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Future模式实现请求的异步回调
 * 
 * @author van
 * 
 */
public class ResponseFuture {
	private static Map<Integer, ResponseFuture> FRTURE = new ConcurrentHashMap<Integer, ResponseFuture>();

	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private JobopResponse response;
	private JobopRequest request;
	private volatile boolean expired = false;

	public ResponseFuture(JobopRequest request) {
		this.request = request;
		FRTURE.put(request.getId(), this);
	}

	public static void receive(JobopResponse response) {
		ResponseFuture future = FRTURE.remove(response.getId());
		try {
			future.getLock().lock();
			future.setResponse(response);
			future.getCondition().signalAll();
		} catch (Exception e) {
		} finally {
			future.getLock().unlock();
		}

	}

	/**
	 * 获取response，同一个对象，无论多少线程，只需要等待一个超时周期，避免由于服务器问题导致的大量阻塞
	 * 
	 * @return
	 */
	public JobopResponse getResponse() {
		// 这里不用response == null判断是因为不可修改response的值
		if (!expired) {
			try {
				// 这里可能会多个线程在此阻塞
				lock.lock();
				// 获取锁后，需要再次判断是否过期了，过期则无需等待，
				if (!expired) {
					try {
						condition.await(request.getReadTimeout(), TimeUnit.MILLISECONDS);
					} catch (InterruptedException e) {
					} finally {
						// 等待了一个超时周期之后(可能被ｓｉｇｎｌ了，或者超时了)，无论还是不是response ==
						// null，都要把expired =true，避免后面线程再进入等待
						expired = true;
					}
				}
			} finally {
				lock.unlock();
			}
		}
		// 等待完成后，response是null，则是超时没收到消息，应把其删除
		if (response == null) {
			try {
				FRTURE.remove(request.getId());
			} catch (Exception e) {
				// do noting 这里catch一下，避免重复删除带来的异常情况
			}
		}
		return response;
	}

	public void setResponse(JobopResponse response) {
		this.response = response;
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

}
