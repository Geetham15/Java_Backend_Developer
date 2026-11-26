package com.example.java8.predicate;

import java.util.function.Predicate;

/**
 * A Predicate is a built-in functional interface in Java 8 that represents a
 * condition and returns either: true or false
 * 
 * In simple words: Predicate checks a condition and tells us whether it is true
 * or false.
 * 
 * If the first Predicate returns false, Java does not evaluate the second
 * Predicate.
 **/
public class PredicateDemo {

	public static void main(String[] args) {
		Predicate<Integer> checkNumber = number -> number > 10;
		System.out.println(checkNumber.test(15)); // Predicate's abstract method test()
		System.out.println(checkNumber.test(5));

		// check the number is even
		Predicate<Integer> checkEvenNumber = number -> (number % 2 == 0);
		System.out.println("Is number even ? : " + checkEvenNumber.test(20));
		System.out.println("Is number even ? : " + checkEvenNumber.test(21));

		// Predicate.and()
		// Number greater than 10 and is even
		Predicate<Integer> greaterThanTen = number -> number > 10;

		Predicate<Integer> isEven = number -> number % 2 == 0;

		Predicate<Integer> result = greaterThanTen.and(isEven);

		System.out.println(result.test(20));
		System.out.println(result.test(15));
		System.out.println(result.test(8));

		// Number greaterthan 50 and divisible by 5
		Predicate<Integer> greaterThanFifty = number -> number > 50;
		Predicate<Integer> isDivisibleByFive = number -> number % 5 == 0;
		Predicate<Integer> result1 = greaterThanFifty.and(isDivisibleByFive);
		System.out.println("Result for greater than 50 and divisible by 5");
		System.out.println(result1.test(75)); // true
		System.out.println(result1.test(40)); // false short-circuit evaluation - first condition fails and Java does
												// not evaluate second condition
		System.out.println(result1.test(55)); // true

		// Predicate.or()
		// Number greater than 100 or divisible by 10
		Predicate<Integer> greaterThanHundred = number -> number > 100;
		Predicate<Integer> isDivisibleByTen = number -> number % 10 == 0;
		Predicate<Integer> result3 = greaterThanHundred.or(isDivisibleByTen);
		System.out.println("Number greater than 100 or divisible by 10");
		System.out.println(result3.test(120)); // true
		System.out.println(result3.test(50)); // true
		System.out.println(result3.test(75)); // false

		// Predicate.negate() - reverse the result of Predicate
		// true becomes false. false becomes true
		Predicate<Integer> greaterThanSixty = number -> number > 60;
		Predicate<Integer> notGreaterThanSixty = greaterThanFifty.negate();
		System.out.println("Predicate Negate result for notGreaterThanSixty");
		System.out.println(notGreaterThanSixty.test(75)); // false
		System.out.println(notGreaterThanSixty.test(30)); // true

		// negate() for divisible by 3
		Predicate<Integer> divisibleByThree = number -> number % 3 == 0;
		Predicate<Integer> notDivisibleByThree = divisibleByThree.negate();
		System.out.println("negate() for divisible by 3");
		System.out.println(notDivisibleByThree.test(9)); //false
		System.out.println(notDivisibleByThree.test(10)); //true
		System.out.println(notDivisibleByThree.test(15)); //false
	}

}
