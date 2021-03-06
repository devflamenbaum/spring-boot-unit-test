package com.devflamenbaum.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
//import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.devflamenbaum.exception.ResourceNotFoundException;
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
	
	@DisplayName("JUNIT Test for saveEmployee Method with Exception")
	@Test
	public void givenEmail_whenSaveEmployee_thenThrowException() {
		
		// given - precondition or setup	
		
		given(employeeRepository.findByEmail(employee.getEmail()))
					.willReturn(Optional.of(employee));		
		
		// when - action or the behavior that we are going to test
		
		org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			employeeService.saveEmployee(employee);
		});
		
		// then - verify the input
		verify(employeeRepository, never()).save(any(Employee.class));
		
	}
	
	@DisplayName("JUNIT Test for getAllEmployees method")
	@Test
	public void givenEmployeesList_whenGetAllEmployees_thenReturnList() {
		
		// given - precondition or setup
		
		Employee employee1 = Employee.builder()
				.id(2L)
				.firstName("Julia")
				.lastName("Prates")
				.email("jup@mailnator.com")
				.build();
		
		given(employeeRepository.findAll())
					.willReturn(List.of(employee, employee1));		
		
		// when - action or the behavior that we are going to test
		
		List<Employee> employeeList = employeeService.getAllEmployees();
		
		// then - verify the input
		Assertions.assertThat(employeeList).isNotNull();
		Assertions.assertThat(employeeList.size()).isEqualTo(2);
	}
	
	@DisplayName("JUNIT Test for getAllEmployees method (NEGATIVE SCENARIO)")
	@Test
	public void givenEmployeesList_whenGetAllEmployees_thenReturnEmptyList() {
		
		// given - precondition or setup
		
		given(employeeRepository.findAll())
					.willReturn(Collections.emptyList());		
		
		// when - action or the behavior that we are going to test
		
		List<Employee> employeeList = employeeService.getAllEmployees();
		
		// then - verify the input
		Assertions.assertThat(employeeList).isEmpty();;
		Assertions.assertThat(employeeList.size()).isEqualTo(0);
	}
	
	@DisplayName("JUNIT Test for getEmployeeById")
	@Test
	public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
		
		// given - precondition or setup
		
		given(employeeRepository.findById(1L))
					.willReturn(Optional.of(employee));		
		
		// when - action or the behavior that we are going to test
		
		Employee employeeDB = employeeService.getEmployeeById(1L).get();
		
		// then - verify the input
		Assertions.assertThat(employeeDB).isNotNull();
		Assertions.assertThat(employeeDB.getId()).isEqualTo(1L);
	}
	
	@DisplayName("JUNIT Test for updateEmployee")
	@Test
	public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
		
		// given - precondition or setup
		
		given(employeeRepository.save(employee))
					.willReturn(employee);		
		
		employee.setEmail("gflamen@gmail.com");
		employee.setFirstName("Biel");
		// when - action or the behavior that we are going to test
		
		Employee updatedEmployee = employeeService.updateEmployee(employee);
		
		// then - verify the input
		Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("gflamen@gmail.com");
		Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Biel");
	}
	
	@DisplayName("JUNIT Test for deleteEmployee")
	@Test
	public void givenEmployeeId_whenDeleteEmployee_thenReturnVoid() {
		
		long employeeId = 1L;
		
		// given - precondition or setup
		
		BDDMockito.willDoNothing().given(employeeRepository).deleteById(employeeId);		

		// when - action or the behavior that we are going to test
		
		employeeService.deleteEmployee(employeeId);
		
		// then - verify the input
		verify(employeeRepository, times(1)).deleteById(employeeId);
		
	}

}
