package com.devflamenbaum.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
//import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devflamenbaum.model.Employee;
import com.devflamenbaum.repository.EmployeeRepository;
import com.devflamenbaum.service.impl.EmployeeServiceImpl;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
	@Mock
	private EmployeeRepository employeeRepository;
	@InjectMocks
	private EmployeeServiceImpl employeeService;
	
	private Employee employee;
	
	@BeforeEach
	public void setup() {
		
		employee = Employee.builder()
				.id(1L)
				.firstName("Gabriel")
				.lastName("Flamenbaum")
				.email("gflamen@mailnator.com")
				.build();
		
		//employeeRepository = Mockito.mock(EmployeeRepository.class);
		//employeeService = new EmployeeServiceImpl(employeeRepository);
	}
	
	@DisplayName("JUNIT Test for saveEmployee Method")
	@Test
	public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
		// given - precondition or setup
		
		given(employeeRepository.findByEmail(employee.getEmail()))
					.willReturn(Optional.empty());
		
		given(employeeRepository.save(employee)).willReturn(employee);

		// when - action or the behavior that we are going to test
		
		Employee savedEmployee = employeeService.saveEmployee(employee);

		// then - verify the input
		
		Assertions.assertThat(savedEmployee).isNotNull();
	}

}
