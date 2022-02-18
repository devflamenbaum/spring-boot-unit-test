package com.devflamenbaum.service;

import java.util.List;

import com.devflamenbaum.model.Employee;

public interface EmployeeService {
	
	Employee saveEmployee(Employee employee);
	
	List<Employee> getAllEmployees();

}
