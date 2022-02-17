package com.devflamenbaum.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import com.devflamenbaum.model.Employee;
import com.devflamenbaum.repository.EmployeeRepository;
import com.devflamenbaum.service.impl.EmployeeServiceImpl;

public class EmployeeServiceTest {
	
	private EmployeeRepository employeeRepository;
	private EmployeeService employeeService;
	
	@BeforeEach
	public void setup() {
		
		employeeRepository = Mockito.mock(EmployeeRepository.class);
		employeeService = new EmployeeServiceImpl(employeeRepository);
	}
	
	@DisplayName("JUNIT Test for saveEmployee Method")
	@Test
	public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
		// given - precondition or setup
		Employee employee = Employee.builder()
								.id(1L)
								.firstName("Gabriel")
								.lastName("Flamenbaum")
								.email("gflamen@mailnator.com")
								.build();
		
		BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
					.willReturn(Optional.empty());
		
		BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
		
		System.out.println(employeeRepository);
		System.out.println(employeeService);

		// when - action or the behavior that we are going to test
		
		Employee savedEmployee = employeeService.saveEmployee(employee);
		
		System.out.println(savedEmployee);

		// then - verify the input
		
		Assertions.assertThat(savedEmployee).isNotNull();
	}

}
