package com.example.employeemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.config.EmployeeProperties;
//Returns configuration through the REST API
@RestController
public class EmployeeConfigController {

	private final EmployeeProperties employeeProperties;

	public EmployeeConfigController(EmployeeProperties employeeProperties) {
		this.employeeProperties = employeeProperties;
	}
	
	@GetMapping("/api/config/employee")
	public EmployeeProperties getEmployeeConfig() {
		return employeeProperties;
	}
	
}
