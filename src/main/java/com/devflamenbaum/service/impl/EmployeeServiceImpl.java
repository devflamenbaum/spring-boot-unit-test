package com.devflamenbaum.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devflamenbaum.exception.ResourceNotFoundException;
import com.devflamenbaum.model.Employee;
import com.devflamenbaum.repository.EmployeeRepository;
import com.devflamenbaum.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	private EmployeeRepository employeeRepository;
	
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}



	@Override
	public Employee saveEmployee(Employee employee) {
		
		Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
		
		if(savedEmployee.isPresent()) {
			throw new ResourceNotFoundException("Employee already exist with given email: " + employee.getEmail());
		}
		
		
		return employeeRepository.save(employee);
	}

}
