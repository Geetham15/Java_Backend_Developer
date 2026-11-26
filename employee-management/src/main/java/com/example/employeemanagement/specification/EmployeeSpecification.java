package com.example.employeemanagement.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.employeemanagement.entity.Employee;

//Specification<Employee> represents one condition used to filter Employee records
public class EmployeeSpecification {

	//checks for specific department
	//SQL : where department='XX'
	public static Specification<Employee> hasDepartment(String department){
		
		//root represents Employee Entity and its field
		//query represents query being constructed
		//createriaBuilder - creates conditions such as equal, greaterThan and lessThan
		return (root, query, criteriaBuilder) -> 
								criteriaBuilder.equal(root.get("department"), department);
	}
	
	// create minimum salary condition
	// SQL: where salary >= xx
	public static Specification<Employee> hasMinimumSalary(BigDecimal minSalary) {

	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.greaterThanOrEqualTo(
	                    root.get("salary"), minSalary);
	}
	
	//create maximum salary condition
	//SQL : where salary<= xx
	public static Specification<Employee> hasMaximumSalary(BigDecimal maxSalary) {

	    return (root, query, criteriaBuilder) ->
	            criteriaBuilder.lessThanOrEqualTo(
	                    root.get("salary"), maxSalary);
	}
	
	//Combine all three above condition
	//GET /api/employees/search-spec?department=IT&minSalary=50000&maxSalary=100000
	//Refer seachEmployeesUsingSpecification() in service
}
