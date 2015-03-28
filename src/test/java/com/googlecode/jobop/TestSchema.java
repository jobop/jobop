package com.googlecode.jobop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.jobop.facade.IHello;

public class TestSchema {
	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:com/googlecode/jobop/test/test-provider.xml");
		context.start();
		IHello h = (IHello) context.getBean("hello1");
		synchronized (TestSchema.class) {
			while (true) {
				try {
					System.out.println(h.say());
					TestSchema.class.wait(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
