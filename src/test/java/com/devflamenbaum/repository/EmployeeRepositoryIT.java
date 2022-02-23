package com.devflamenbaum.repository;

import java.util.List;
import java.util.Optional;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.devflamenbaum.model.Employee;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIT {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee employee;
	
	@BeforeEach
	public void setup() {
		employee = Employee.builder()
				.firstName("Gabriel")
				.lastName("Flamenbaum")
				.email("gflamen@mailnator.com")
				.build();
	}
	
	//JUnit Test for save employee operation
	@Test
	public void givenEmployeeObject_whenSave_thenReturnSavedObject() {
		
		// given - precondition or setup
		
		
		// when - action or the behavior that we are going to test
		
		Employee savedEmployee = employeeRepository.save(employee);
		
		// then - verify the input
		
		Assertions.assertThat(savedEmployee).isNotNull();
		Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
	}
	
	@Test
	public void givenEmployees_whenFindAll_thenEmployeeList() {
		// given - precondition or setup
		
		Employee employee1 = Employee.builder()
				.firstName("Julia")
				.lastName("Prates")
				.email("juprates@mailnator.com")
				.build();
		
		employeeRepository.save(employee);
		employeeRepository.save(employee1);

		// when - action or the behavior that we are going to test
		
		List<Employee> employeeList = employeeRepository.findAll();

		// then - verify the input
		
		Assertions.assertThat(employeeList).isNotNull();
		Assertions.assertThat(employeeList.size()).isEqualTo(2);
	}
	
	@Test
	public void givenEmployeeObject_whenFindById_thenEmployeeObject() {
		// given - precondition or setup
		
		employeeRepository.save(employee);

		// when - action or the behavior that we are going to test
		Employee employeeDB = employeeRepository.findById(employee.getId()).get();

		// then - verify the input
		Assertions.assertThat(employeeDB).isNotNull();
	}
	
	@Test
	public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeOject() {
		// given - precondition or setup
		
		employeeRepository.save(employee);
		// when - action or the behavior that we are going to test
		Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

		// then - verify the input
		Assertions.assertThat(employeeDB).isNotNull();
	}
	
	@Test
	public void givenEmployee_whenUpdateEmployee_thenReturmUpdatedEmployee() {
		// given - precondition or setup
		
		employeeRepository.save(employee);

		// when - action or the behavior that we are going to test
		Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
		savedEmployee.setEmail("gflamen@gmail.com");
		savedEmployee.setFirstName("Biel");
		
		Employee updatedEmployee = employeeRepository.save(savedEmployee);

		// then - verify the input
		Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("gflamen@gmail.com");
		Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Biel");
	}
	
	@Test
	public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
		// given - precondition or setup
		
		employeeRepository.save(employee);

		// when - action or the behavior that we are going to test
		
		employeeRepository.delete(employee);
		Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());
		// then - verify the input
		Assertions.assertThat(employeeOptional).isEmpty();
	}
	
	@Test
	public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
		// given - precondition or setup
		
		employeeRepository.save(employee);
		
		String firstName = "Gabriel";
		String lastName = "Flamenbaum";
		// when - action or the behavior that we are going to test
		Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);		

		// then - verify the input
		Assertions.assertThat(savedEmployee).isNotNull();
	}
	
	@Test
	public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
		// given - precondition or setup
		
		employeeRepository.save(employee);
		
		String firstName = "Gabriel";
		String lastName = "Flamenbaum";
		// when - action or the behavior that we are going to test
		Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);		

		// then - verify the input
		Assertions.assertThat(savedEmployee).isNotNull();
	}
	
	@Test
	public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
		// given - precondition or setup
		
		employeeRepository.save(employee);
		
		String firstName = "Gabriel";
		String lastName = "Flamenbaum";
		// when - action or the behavior that we are going to test
		Employee savedEmployee = employeeRepository.findByNativeSQL(firstName, lastName);		

		// then - verify the input
		Assertions.assertThat(savedEmployee).isNotNull();
	}
}
