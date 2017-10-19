package io.javabrains;

public class HelloWorldGreetings implements Greeting {

	@Override
	public void perform() {
		System.out.print("HelloWorld!");
	}

}
