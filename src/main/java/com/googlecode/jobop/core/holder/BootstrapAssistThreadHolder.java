package com.googlecode.jobop.core.holder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TODO:how to detroy it
public class BootstrapAssistThreadHolder {
	private ExecutorService executorService = null;
	private static BootstrapAssistThreadHolder bootstrapThreadHolder = null;

	private BootstrapAssistThreadHolder() {
		executorService = Executors.newFixedThreadPool(2);
	}

	public static BootstrapAssistThreadHolder getInstance() {
		if (null == bootstrapThreadHolder) {
			synchronized (BootstrapAssistThreadHolder.class) {
				if (null == bootstrapThreadHolder) {
					bootstrapThreadHolder = new BootstrapAssistThreadHolder();
				}
			}
		}
		return bootstrapThreadHolder;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}
}
