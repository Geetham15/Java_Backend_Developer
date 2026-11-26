package com.example.java8.functional;

public class FunctionalInterfaceDemo {

	public static void main(String[] args) {
		Greeting greeting = (name) -> {
				System.out.println("Welcome " + name + "! Happy Learning Java 21.");
			};
		
		greeting.sayHello("Geetha");
		
		//Two-parameter Lambda function
		Calculator calculator = (a, b) -> a+b;
		int result = calculator.add(10, 20);
		System.out.println("Addition: " + result);
		
		
		
	}
}
