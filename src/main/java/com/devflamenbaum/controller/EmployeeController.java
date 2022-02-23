package com.devflamenbaum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devflamenbaum.model.Employee;
import com.devflamenbaum.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	
	private EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}
	
	@GetMapping
	public List<Employee> getAllEmployees(){
		return employeeService.getAllEmployees();
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id){
		return employeeService.getEmployeeById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable("id") long employeeId, @RequestBody Employee employee){
		return employeeService.getEmployeeById(employeeId)
				.map(savedEmployee -> {
					savedEmployee.setFirstName(employee.getFirstName());
					savedEmployee.setLastName(employee.getLastName());
					savedEmployee.setEmail(employee.getEmail());
					
					Employee updatedEmployee = employeeService.updateEmployee(savedEmployee);
					return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
				}).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
