package com.example.java8.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EmployeePredicateDemo {

	public static void main(String[] args) {
		List<Employee> employees = new ArrayList<>(Arrays.asList(new Employee(1, "John", "IT", 60000),
				new Employee(2, "David", "HR", 40000), new Employee(3, "Mary", "IT", 75000),
				new Employee(4, "Peter", "Finance", 45000), new Employee(5, "Sarah", "HR", 65000)));

		// Predicate: Salary greater than 50000
		Predicate<Employee> highSalary = emp -> emp.getSalary() > 50000;

		// Filter employees
		employees.stream() // creates stream from employee list
				.filter(highSalary) // accepts Predicate and filters employees
				.forEach(System.out::println); // prints the matching employees

		// Find employee in IT department and salary > 50000
		Predicate<Employee> isIT = emp -> emp.getDepartment().equals("IT");
		Predicate<Employee> salaryAbove50K = emp -> emp.getSalary() > 50000;
		Predicate<Employee> itHighSalary = isIT.and(salaryAbove50K); // Predicate composition
		System.out.println("employee in IT department and salary > 50000");
		employees.stream().filter(itHighSalary).forEach(System.out::println);

		/*
		 * Find employees who belong to either: - IT department with salary greater than
		 * 50,000, OR - HR department with salary greater than 60,000.
		 */
		Predicate<Employee> isHR = emp -> emp.getDepartment().equals("HR");
		Predicate<Employee> salaryAbove60K = emp -> emp.getSalary() > 60000;
		Predicate<Employee> hrHighSalary = isHR.and(salaryAbove60K);

		Predicate<Employee> highSalaryResult = itHighSalary.or(hrHighSalary);
		System.out.println("List of IT and HR High Salary");
		employees.stream().filter(highSalaryResult).forEach(System.out::println);

		// Predicate.negate()
		// Find all employees who does not belong to the IT department
		// Predicate<Employee> isITEmp = emp -> emp.getDepartment().equals("IT");

		Predicate<Employee> isNotIT = isIT.negate();
		System.out.println("List of all Non IT Department employee");
		employees.stream().filter(isNotIT).forEach(System.out::println);

		// Find employees who are NOT in HR department AND have a salary greater than
		// 45,000.
		// Predicate<Employee> isHR = emp -> emp.getDepartment().equals("HR");
		Predicate<Employee> salaryAbove45k = emp -> emp.getSalary() > 45000;
		Predicate<Employee> nonHR = isHR.negate();
		Predicate<Employee> nonHRHighSalary = nonHR.and(salaryAbove45k);
		System.out.println("employees who are NOT in HR department AND have a salary greater than 45,000");
		employees.stream().filter(nonHRHighSalary).forEach(System.out::println);

		/**
		 * //Predicate with removeIF() //Remove all employees belong to HR department
		 * from a list System.out.println("Remove all employees belong to HR department
		 * from a list"); employees.removeIf( emp -> emp.getDepartment().equals("HR") );
		 * 
		 * employees.forEach(System.out::println);
		 * 
		 * //Remove all employees whose salary is less than 50,000
		 * System.out.println("Remove all employees whose salary is less than 50,000");
		 * employees.removeIf(emp -> emp.getSalary() < 50000);
		 * employees.forEach(System.out::println);
		 */
		System.out.println("Employee Names");
		employees.stream().map(emp -> emp.getName()).forEach(System.out::println);
		
		// Increase every employee's salary by 10%.
		System.out.println("Increase every employee's salary by 10%.");
		employees.stream().map(emp -> emp.getSalary() * 1.10).forEach(System.out::println);

		// sort employee salary in asc order
		System.out.println("sort employee salary in asc order");
		employees.stream().map(emp -> emp.getSalary()).sorted().forEach(System.out::println);
		
		//Sort employee salaries in descending order
		System.out.println("Sort employee salaries in descending order");
		employees.stream()
	        .map(emp -> emp.getSalary())
	        .sorted(Comparator.reverseOrder())
	        .forEach(System.out::println);
		//Sort Employee objects by salary
		System.out.println("Sort Employee objects by salary");
		employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary))
        .forEach(System.out::println);
		//Sort Employee objects by salary descing
		System.out.println("//Sort Employee objects by salary descending");
		employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary).reversed())
        .forEach(System.out::println);
		 //collect()
		//Collect employee names into a List.
		System.out.println("Collect employee names into a List.");
		List<String> employeeNames =
		        employees.stream()
		                .map(Employee::getName)
		                .collect(Collectors.toList());

		System.out.println(employeeNames);
		//Collectors.groupingBy()
		//Group employees by department
		System.out.println("Group employees by department");
		Map<String, List<Employee>> employeesByDepartment = employees.stream()
				.collect(Collectors.groupingBy(Employee::getDepartment));
		employeesByDepartment.forEach((department,employeeList) -> {
			System.out.println(department);
			employeeList.forEach(System.out::println);
		});
		
		//employees.stream().map(emp -> emp.getName().toUpperCase()).forEach(System.out::println);
		//getEmployee name in all caps
		//type transformation
		employees.stream().map(Employee::getName).map(String::toUpperCase).forEach(System.out::println);
		
		//Display employees in ascending order of salary
		System.out.println("Display employees in ascending order of salary");
		employees.stream().sorted(Comparator.comparing(Employee::getSalary)).forEach(System.out::println);
		
		//Convert all employee names to uppercase and collect them into a List<String>
		System.out.println("Convert all employee names to uppercase and collect them into a List<String>");
		//List<String> empNameinUpperList = employees.stream().map(Employee::getName).map(String::toUpperCase).collect(Collectors.toList());
		List<String> empNameinUpperList = employees.stream().map(emp->emp.getName().toUpperCase()).collect(Collectors.toList());
		System.out.println(empNameinUpperList);
		//Collect only employees from the IT department into a new List<Employee>
		System.out.println("Collect only employees from the IT department into a new List<Employee>");
		
		List<Employee> ITEmp = employees.stream().filter(emp -> emp.getDepartment().equals("IT")).collect(Collectors.toList());
		//System.out.println(ITEmp);
		ITEmp.forEach(System.out::println);
		
		//Group employees by department and print each department with its employees
		System.out.println("Group employees by department and print each department with its employees");
		Map<String, List<Employee>> employeesByDepart = employees.stream()
				.collect(Collectors.groupingBy(Employee::getDepartment));
		employeesByDepart.forEach((department,employeeList) -> {
			System.out.println("Department : " + department);
			employeeList.forEach(emp -> System.out.println(emp.getName()));
		});
		
		//Find the employee who has the highest salary
		System.out.println("Find the employee who has the highest salary");
		Optional<Employee> highestPaidEmployee = employees.stream().max(Comparator.comparing(Employee::getSalary));
		highestPaidEmployee.ifPresent(System.out::println);
		
		//Employee with lowest salary 
		System.out.println("Employee with lowest salary");
		Optional<Employee> lowestSalaryPaid = employees.stream().min(Comparator.comparing(Employee::getSalary));
		lowestSalaryPaid.ifPresent(System.out::println);
		//reduce() - used to combine stream elements into single result
		
		//total salary of all employee
		double totalsalary = employees.stream().mapToDouble(Employee::getSalary).sum();
		System.out.println("Total salary : " + totalsalary);
		
		double averageSalary = employees.stream().mapToDouble(Employee::getSalary).average().orElse(0.00);
		System.out.println("Average salary : "+ averageSalary);
		//Count the number of employees in the IT department
		long count = employees.stream().filter(emp -> emp.getDepartment().equals("IT")).count();
		System.out.println("Count of employess in IT department " + count);
		
		
	}

}
