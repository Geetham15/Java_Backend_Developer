package com.example.java8.predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
// Function accepts one input and produces one output.
public class FunctionExample {

	public static void main(String[] args) {
		Function<String, String> convertToUppercase = name -> name.toUpperCase();
		String result = convertToUppercase.apply("geetha");
		System.out.println(result);
		
		Function<Employee, String> getEmployeeName = employee -> employee.getName();
		String name = getEmployeeName.apply(new Employee(1, "John", "IT", 60000));
		System.out.println(name);
		
		//salary with 10%increment
		Function<Double, Double> incrementSalary = salary -> salary * 1.10;
		double updatedSalary = incrementSalary.apply(60000.0);
		System.out.println(updatedSalary);
		
		//Consumer with string
		Consumer<String> printName = cname -> System.out.println(cname);
		printName.accept("John");

		//Consumer with Employee
		Consumer<Employee> employeeDetails = employee -> {
		    System.out.println("Employee ID: " + employee.getId());
		    System.out.println("Employee Name: " + employee.getName());
		    System.out.println("Department: " + employee.getDepartment());
		    System.out.println("Salary: " + employee.getSalary());
		};
		Employee employee = new Employee(1, "John", "IT", 60000);
		employeeDetails.accept(employee);
		
		//Consumer with Collection
		List<Employee> employees = new ArrayList<>();
		employees.add(employee);
		employees.forEach(e -> System.out.println(e.getName()));
		
		//Consumer chaining
		System.out.println("Consumer chaining");
		Consumer<Employee> cprintName =
		        emp -> System.out.println(emp.getName());
		Consumer<Employee> printSalary = emp -> System.out.println(emp.getSalary());
		Consumer<Employee> empDetails =
		        cprintName.andThen(printSalary);
		Employee employee1 = new Employee(1, "AAA", "IT", 69000);
		empDetails.accept(employee1);

	}

}
