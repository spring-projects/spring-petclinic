package io.javabrains;

public class Greeter {
	public void greet(Greeting greeting) {
		greeting.perform();
	}
	
	public static void main(String[] args) {
		Greeter greeter = new Greeter();
		HelloWorldGreetings helloWorldGreeting = new HelloWorldGreetings();
		 greeter.greet(helloWorldGreeting);
		 
		 Greeting myLambdaFunction = ()-> System.out.print("HelloWorld");
	}

}

