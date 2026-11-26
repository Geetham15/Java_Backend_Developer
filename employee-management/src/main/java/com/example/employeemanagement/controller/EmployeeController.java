package com.example.employeemanagement.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeemanagement.dto.EmployeeCreateDTO;
import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.dto.EmployeeUpdateDTO;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;

import jakarta.validation.Valid;

//ResponseEntity allow us to control HTTP status codes and response bodies.
@RestController
@RequestMapping("/api/employees") // defines base url
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		super();
		this.employeeService = employeeService;
	}

	// 1. Create employee
	@PostMapping // Handles HTTP POST requests
	public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeCreateDTO dto) {
		Employee savedEmployee = employeeService.createEmployee(dto);
		EmployeeDTO response = employeeService.convertToDTO(savedEmployee);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// 2. Get all employees
	 /**@GetMapping // Handles HTTP GET requests
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}**/
	
	@GetMapping
	public Page<Employee> getAllEmployees(
	        @PageableDefault(size = 5) Pageable pageable) {

	    return employeeService.getAllEmployees(pageable);
	}
	
	@GetMapping("/slice")
	public Slice<Employee> getEmployeesAsSlice(
	        @PageableDefault(size = 5) Pageable pageable) {

	    return employeeService.getEmployeesAsSlice(pageable);
	}
	// 3. Get Employee by id
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
		EmployeeDTO employeeDTO = employeeService.getEmployeeByIdDTO(id);
		return ResponseEntity.ok(employeeDTO);
	}

	// 4. Update Employee
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, 
			@Valid @RequestBody EmployeeUpdateDTO dto) {
		Employee updatedemployee = employeeService.updateEmployee(id, dto);
		EmployeeDTO response = employeeService.convertToDTO(updatedemployee);
		return ResponseEntity.ok(response);
	}

	// 5. Delete Employee
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return ResponseEntity.noContent().build();
	}

	// 6. Find employee by email
	@GetMapping("/email/{email}")
	public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
		Employee employee = employeeService.getEmployeeByEmail(email);
		return ResponseEntity.ok(employee);
	}

	// 7. Find employees by department
	@GetMapping("/department/{department}")
	public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepatment(@PathVariable String department) {
		List<EmployeeDTO> employeeDTO = employeeService.getEmployeesByDepartmentDTO(department);
		return ResponseEntity.ok(employeeDTO);
	}

	// 8. Find employees by salary greather than
	@GetMapping("/salary/{salary}")
	public ResponseEntity<List<Employee>> getEmployeesBySalaryGreaterThan(@PathVariable BigDecimal salary) {
		List<Employee> employees = employeeService.getEmployeesBySalaryGreaterThan(salary);
		return ResponseEntity.ok(employees);
	}
	@GetMapping("/dto")
	public List<EmployeeDTO> getAllEmployeesDTO() {
	    return employeeService.getAllEmployeesDTO();
	}
	@GetMapping("/dto/paged")
	public Page<EmployeeDTO> getAllEmployeesDTO(Pageable pageable) {
	    return employeeService.getAllEmployeesDTO(pageable);
	}
	@GetMapping("/dto/sorted")
	public Page<EmployeeDTO> getEmployeesSortBySalary(Pageable pageable){
		return employeeService.getEmployeesSortedBySalary(pageable);
	}
	
	//@RequestParam(required=false) - parameter is optional
	@GetMapping("/search")
	public List<EmployeeDTO> searchEmployees(
			@RequestParam(required=false) String department,
			@RequestParam(required=false) BigDecimal minSalary,
			@RequestParam(required=false) BigDecimal maxSalary){
		
		return employeeService.searchEmployees(department, minSalary, maxSalary);
	}
	@GetMapping("/search-spec")
	public List<EmployeeDTO> searchEmployeesUsingSpecifications(
	        @RequestParam(required = false) String department,
	        @RequestParam(required = false) BigDecimal minSalary,
	        @RequestParam(required = false) BigDecimal maxSalary) {

	    return employeeService.searchEmployeesUsingSpecifications(
	            department, minSalary, maxSalary);
	}
	@GetMapping("/search-sort")
	public List<EmployeeDTO> searchAndSortEmployees(
	        @RequestParam(required = false) String department,
	        @RequestParam(required = false) BigDecimal minSalary,
	        @RequestParam(required = false) BigDecimal maxSalary,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    return employeeService.searchAndSortEmployees(
	            department, minSalary, maxSalary, sortBy, direction);
	}
	@GetMapping("/search-page")
	public Page<EmployeeDTO> searchEmployeesWithPagination(
	        @RequestParam(required = false) String department,
	        @RequestParam(required = false) BigDecimal minSalary,
	        @RequestParam(required = false) BigDecimal maxSalary,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size,
	        @RequestParam(defaultValue = "id") String sortBy,
	        @RequestParam(defaultValue = "asc") String direction) {

	    return employeeService.searchEmployeesWithPagination(
	            department, minSalary, maxSalary,
	            page, size, sortBy, direction);
	}
	
}
