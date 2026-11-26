package com.example.java8.functional;

@FunctionalInterface
public interface Greeting {

	void sayHello(String name); //exactly one abstract method
}
