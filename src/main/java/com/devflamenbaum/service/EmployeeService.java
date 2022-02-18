package com.devflamenbaum.service;

import java.util.List;
import java.util.Optional;

import com.devflamenbaum.model.Employee;

public interface EmployeeService {
	
	Employee saveEmployee(Employee employee);
	
	List<Employee> getAllEmployees();
	
	Optional<Employee> getEmployeeById(Long id);
	
	Employee updateEmployee(Employee employee);

}
