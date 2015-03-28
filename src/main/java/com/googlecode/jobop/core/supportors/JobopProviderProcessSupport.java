package com.googlecode.jobop.core.supportors;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.netty.channel.ChannelHandlerContext;

import com.googlecode.jobop.bootstrap.JobopBootstrap;
import com.googlecode.jobop.common.utils.DclSingletonUtil;
import com.googlecode.jobop.connector.exchange.JobopRequest;
import com.googlecode.jobop.connector.exchange.JobopResponse;
import com.googlecode.jobop.core.domain.Consumer;
import com.googlecode.jobop.core.domain.Provider;
import com.googlecode.jobop.core.holder.ProvidersHolder;
import com.googlecode.jobop.core.protocol.handlers.JobopRequestProcessor;

/**
 * Jobop服务提供者任务处理支撑
 * 
 * @author van
 * 
 */
public class JobopProviderProcessSupport implements JobopBootstrap, JobopRequestProcessor {

	public static void main(String[] args) {
		JobopConsumerSubcribeSupport jobopConsumerSubcribeSupport1 = DclSingletonUtil.getSingleton(JobopConsumerSubcribeSupport.class);
		JobopConsumerSubcribeSupport jobopConsumerSubcribeSupport2 = DclSingletonUtil.getSingleton(JobopConsumerSubcribeSupport.class);
		JobopConsumerSubcribeSupport jobopConsumerSubcribeSupport3 = DclSingletonUtil.getSingleton(JobopConsumerSubcribeSupport.class);
		JobopConsumerSubcribeSupport jobopConsumerSubcribeSupport4 = DclSingletonUtil.getSingleton(JobopConsumerSubcribeSupport.class);

		// jobopConsumerSubcribeSupport = getInstance();
		System.out.println(jobopConsumerSubcribeSupport1);
		System.out.println(jobopConsumerSubcribeSupport2);
		System.out.println(jobopConsumerSubcribeSupport3);
		System.out.println(jobopConsumerSubcribeSupport4);
	}

	private AtomicLong startTime = new AtomicLong(0);
	/** 用来处理业务的线程池 */
	private static ExecutorService processExecutor;
	@SuppressWarnings("unused")
	private static JobopProviderProcessSupport jobopProviderProcessSupport;

	private JobopProviderProcessSupport() {
	}

	@Override
	public void init() {
		// 初始化一个线程池，用来处理业务，代码只执行一次
		if (startTime.compareAndSet(0, System.currentTimeMillis())) {
			processExecutor = Executors.newCachedThreadPool(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable runnable) {
					return new Thread(runnable, "jobop-process-task");
				}
			});
		}

	}

	@Override
	public void shutdown() {
		// 销毁线程池，代码只执行一次
		if (!startTime.compareAndSet(0, 0)) {
			processExecutor.shutdown();
		}

	}

	@Override
	public void doProcess(final ChannelHandlerContext ctx, final JobopRequest request) {
		Consumer consumer = (Consumer) request.getContent();
		Provider relProvider = ProvidersHolder.getInstance().getProviders().get(consumer.getKey());
		if (null == relProvider) {
			// TODO:抛异常
		}
		final Object invokeObj = relProvider.getProxyObj();
		final Class<?> invokeClazz = relProvider.getInterfaceClazz();
		final String methodName = relProvider.getMethodName();
		final Class<?>[] arguTypes = relProvider.getMethodArgusTypes();
		final Object[] argus = consumer.getMethodArgus();
		processExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Object result = invokeClazz.getMethod(methodName, arguTypes).invoke(invokeObj, argus);
					JobopResponse response = new JobopResponse();
					response.setResultObj(result);
					response.setId(request.getId());
					ctx.getChannel().write(response);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void startup() {

	}

}
