package com.googlecode.jobop.core.holder;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.jobop.core.domain.Consumer;

public class ConsumerHolder {
	private static ConsumerHolder consumerHolder = null;
	private Map<String, Consumer> consumers = new HashMap<String, Consumer>();

	public static ConsumerHolder getInstance() {
		if (null == consumerHolder) {
			synchronized (ConsumerHolder.class) {
				if (null == consumerHolder) {
					consumerHolder = new ConsumerHolder();
				}
			}
		}
		return consumerHolder;
	}

	public void addConsumer(String key, Consumer consumer) {
		consumers.put(key, consumer);
	}

	public static ConsumerHolder getConsumerHolder() {
		return consumerHolder;
	}

	public static void setConsumerHolder(ConsumerHolder consumerHolder) {
		ConsumerHolder.consumerHolder = consumerHolder;
	}

	public Map<String, Consumer> getConsumers() {
		return consumers;
	}

	public void setConsumers(Map<String, Consumer> consumers) {
		this.consumers = consumers;
	}

}
