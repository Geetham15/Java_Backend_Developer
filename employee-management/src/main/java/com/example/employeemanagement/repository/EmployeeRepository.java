package com.example.employeemanagement.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.employeemanagement.entity.Employee;

//Repository layer is responsible for communicating with the database.
//Spring Data JPA generates the query automatically based on the methodName

//JpaRepository - Standard CRUD, pagination and sorting
//JpaSpecificationExecutor - Dynamic queries using specifications
@Repository
public interface EmployeeRepository 
		extends JpaRepository<Employee, Long>,
				JpaSpecificationExecutor<Employee>{

	//find employee by email
	Optional<Employee> findByEmail(String email);
	
	//find employees by department
	List<Employee> findByDepartment(String department);
	
	//find employee who receives salary greater than xxx
	List<Employee> findBySalaryGreaterThan(BigDecimal salary);
	
	@Query("SELECT e FROM Employee e")
    Slice<Employee> findEmployeesAsSlice(Pageable pageable);
	
	//find employee by department yy and salary greather than xxx
	//List<Employee> findByDepartmentAndSalaryGreaterThan(String department, BigDecimal salary);
	
	//GET /api/employees/search?department=IT&minSalary=50000&maxSalary=100000
	//JPQL - uses Java Entity name and its field names
	//@Param -  connects Java method parameters to query parameters
	@Query("""
			SELECT e from Employee e
			WHERE (:department IS NULL OR e.department = :department)
			AND (:minSalary IS NULL or e.salary >= :minSalary)
			AND (:maxSalary IS NULL or e.salary <= :maxSalary)
			""")
	List<Employee> searchEmployees(
			@Param("department") String department,
			@Param("minSalary") BigDecimal minSalary,
			@Param("maxSalary") BigDecimal maxSalary
			);
	
	//NO need to write implementation classes for standard database operations 
	//Spring Data JPA automatically creates a proxy implementation of the repository interface at runtime. 
	//It provides implementations for the inherited repository methods and executes the corresponding database operations.
	/**
	 * save(employee)
	 * findAll()
	 * findById(id)
	 * existsById(id)
	 * deleteById(id)
	 * count()
	 */
}
