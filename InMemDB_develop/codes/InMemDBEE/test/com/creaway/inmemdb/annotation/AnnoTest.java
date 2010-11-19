package com.creaway.inmemdb.annotation;

public class AnnoTest {

	@Interceptor(intercept = false)
	public void doSomething3() {
		System.out.println("shit!");
	}

	public static void main(String[] xx) {
		new AnnoTest().doSomething3();
	}

}
