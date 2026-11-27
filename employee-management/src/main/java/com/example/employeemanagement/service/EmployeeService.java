package com.example.employeemanagement.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.specification.EmployeeSpecification;
import com.example.employeemanagement.dto.EmployeeCreateDTO;
import com.example.employeemanagement.dto.EmployeeDTO;
import com.example.employeemanagement.dto.EmployeeUpdateDTO;

// Service layer contains the business logic of our application
//CRUD Operations in Service
@Service
public class EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	private final EmployeeRepository employeeRepository;

	// Constructor Injection
	public EmployeeService(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}

	// Create employee
	public Employee saveEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	// Get all employees
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	public Page<Employee> getAllEmployees(Pageable pageable) {
		return employeeRepository.findAll(pageable);
	}

	public Slice<Employee> getEmployeesAsSlice(Pageable pageable) {
		return employeeRepository.findEmployeesAsSlice(pageable);
	}

	// Get employee by Id
	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
	}

	// Update Employee
	public Employee updateEmployee(Long id, EmployeeUpdateDTO dto) {
		// Get the current details of the employee
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
		// update employee details
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setEmail(dto.getEmail());
		employee.setDepartment(dto.getDepartment());
		employee.setSalary(dto.getSalary());
		return employeeRepository.save(employee);
	}

	// Delete employee
	public void deleteEmployee(Long id) {
		Employee employee = getEmployeeById(id);
		employeeRepository.delete(employee);
	}

	// find employee by email
	public Employee getEmployeeByEmail(String email) {
		return employeeRepository.findByEmail(email)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with email: " + email));
	}

	// find employees by department
	public List<Employee> getEmployeesByDepartment(String department) {
		return employeeRepository.findByDepartment(department);
	}

	// find employee who gets salary greather than 70k
	public List<Employee> getEmployeesBySalaryGreaterThan(BigDecimal salary) {
		return employeeRepository.findBySalaryGreaterThan(salary);
	}

	// Employee Entity values are copied to DTO constructor.
	public EmployeeDTO convertToDTO(Employee employee) {

		return new EmployeeDTO(employee.getId(), employee.getFirstName(), employee.getLastName(),
				employee.getDepartment());
	}

	// to return EmployeeDTO
	public List<EmployeeDTO> getAllEmployeesDTO() {
		return employeeRepository.findAll().stream().map(this::convertToDTO).toList();
	}

	public Page<EmployeeDTO> getAllEmployeesDTO(Pageable pageable) {
		return employeeRepository.findAll(pageable).map(this::convertToDTO);
	}

	// convert EmployeeCreateDTO into Employee entity
	public Employee convertToEntity(EmployeeCreateDTO dto) {
		Employee employee = new Employee();
		employee.setFirstName(dto.getFirstName());
		employee.setLastName(dto.getLastName());
		employee.setEmail(dto.getEmail());
		employee.setDepartment(dto.getDepartment());
		employee.setSalary(dto.getSalary());
		return employee;
	}

	public Employee createEmployee(EmployeeCreateDTO dto) {
		logger.info("Creating a new employee");
		Employee employee = convertToEntity(dto);
		Employee savedEmployee = employeeRepository.save(employee);
		logger.info("Employee created successfully with ID: {}", savedEmployee.getId());
		return savedEmployee;
	}

	public EmployeeDTO getEmployeeByIdDTO(Long id) {
		
		logger.trace("TRACE: Fetching employee");
		logger.debug("DEBUG: Fetching employee");
		logger.info("Fetching employee with ID : {}", id);
		logger.info("INFO: Fetching employee");
		logger.warn("WARN: Fetching employee");
		logger.error("ERROR: Fetching employee");
		Employee employee = getEmployeeById(id);
		logger.info("Employee retrieved successfully with ID: {} ", id);
		return convertToDTO(employee);
	}

	public List<EmployeeDTO> getEmployeesByDepartmentDTO(String department) {

		return employeeRepository.findByDepartment(department).stream().map(this::convertToDTO).toList();
	}

	// sort by hidden data field
	// Sorting happens using Employee Entity and database query before converting
	// the results into EmployeeDTO
	public Page<EmployeeDTO> getEmployeesSortedBySalary(Pageable pageable) {
		return employeeRepository.findAll(pageable).map(this::convertToDTO);
	}

	public List<EmployeeDTO> searchEmployees(String department, BigDecimal minSalary, BigDecimal maxSalary) {

		return employeeRepository.searchEmployees(department, minSalary, maxSalary).stream().map(this::convertToDTO)
				.toList();
	}

	public List<EmployeeDTO> searchEmployeesUsingSpecifications(String department, BigDecimal minSalary,
			BigDecimal maxSalary) {
		// Start with no filtering condition
		Specification<Employee> spec = Specification.unrestricted();
		if (department != null && !department.isBlank()) {
			spec = spec.and(EmployeeSpecification.hasDepartment(department));
		}
		// Add minimum salary condition only when provided
		if (minSalary != null) {
			spec = spec.and(EmployeeSpecification.hasMinimumSalary(minSalary));
		}

		// Add maximum salary condition only when provided
		if (maxSalary != null) {
			spec = spec.and(EmployeeSpecification.hasMaximumSalary(maxSalary));
		}
		return employeeRepository.findAll(spec).stream().map(this::convertToDTO).toList();
	}

	// TEST GET :
	// http://localhost:8080/api/employees/search-sort?sortBy=salary&direction=desc
	public List<EmployeeDTO> searchAndSortEmployees(String department, BigDecimal minSalary, BigDecimal maxSalary,
			String sortBy, String direction) {

		Specification<Employee> spec = Specification.unrestricted();

		if (department != null && !department.isBlank()) {
			spec = spec.and(EmployeeSpecification.hasDepartment(department));
		}

		if (minSalary != null) {
			spec = spec.and(EmployeeSpecification.hasMinimumSalary(minSalary));
		}

		if (maxSalary != null) {
			spec = spec.and(EmployeeSpecification.hasMaximumSalary(maxSalary));
		}
		// Set default sorting field if missing
		if (sortBy == null || sortBy.isBlank()) {
			sortBy = "id";
		}
		// Define allowed sorting fields to avoid any unKnownfield
		Set<String> allowedSortFields = Set.of("id", "firstName", "lastName", "department", "salary");
		// Validate requested sorting field
		if (!allowedSortFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sorting field: " + sortBy);
		}

		if (direction == null || direction.isBlank()) {
			direction = "asc";
		}
		if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sort direction. Use asc or desc");
		}
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		return employeeRepository.findAll(spec, sort).stream().map(this::convertToDTO).toList();
	}

	// Pagination combined with filtering
	// JPASpecifications - dynamic filtering
	// Pageable - pagination and sorting
	// TEST GET
	// /api/employees/search-page?department=IT&minSalary=50000&page=0&size=5&sortBy=salary&direction=desc
	public Page<EmployeeDTO> searchEmployeesWithPagination(String department, BigDecimal minSalary,
			BigDecimal maxSalary, int page, int size, String sortBy, String direction) {

		// Validate pagination parameters
		if (page < 0 || size < 1 || size > 100) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Page must be >= 0 and size must be between 1 and 100");
		}

		// Default sorting field
		if (sortBy == null || sortBy.isBlank()) {
			sortBy = "id";
		}

		// Allowed sorting fields
		Set<String> allowedSortFields = Set.of("id", "firstName", "lastName", "department", "salary");

		if (!allowedSortFields.contains(sortBy)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sorting field: " + sortBy);
		}

		// Default sorting direction
		if (direction == null || direction.isBlank()) {
			direction = "asc";
		}

		if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid sort direction. Use asc or desc.");
		}

		// Build sorting
		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		// Build pagination
		Pageable pageable = PageRequest.of(page, size, sort);

		// Build dynamic filters
		Specification<Employee> spec = Specification.unrestricted();

		if (department != null && !department.isBlank()) {
			spec = spec.and(EmployeeSpecification.hasDepartment(department));
		}

		if (minSalary != null) {
			spec = spec.and(EmployeeSpecification.hasMinimumSalary(minSalary));
		}

		if (maxSalary != null) {
			spec = spec.and(EmployeeSpecification.hasMaximumSalary(maxSalary));
		}

		// Fetch paginated results and convert to DTO
		return employeeRepository.findAll(spec, pageable).map(this::convertToDTO);
	}

}
