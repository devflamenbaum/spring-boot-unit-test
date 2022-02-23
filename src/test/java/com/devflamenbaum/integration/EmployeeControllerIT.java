package com.devflamenbaum.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.devflamenbaum.model.Employee;
import com.devflamenbaum.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Testcontainers
public class EmployeeControllerIT extends AbstractBaseTest{
	
	/*
	 * @Container private static final MySQLContainer mySQLContainer = new
	 * MySQLContainer("mysql:latest") .withUsername("username")
	 * .withPassword("password") .withDatabaseName("ems");
	 * 
	 * @DynamicPropertySource public static void
	 * dynamicPropertySource(DynamicPropertyRegistry registry) {
	 * registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
	 * registry.add("spring.datasource.username", mySQLContainer::getUsername);
	 * registry.add("spring.datasource.password", mySQLContainer::getPassword); }
	 */
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	public void setup() {
		employeeRepository.deleteAll();
	}
	
	@Test
	public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
		

		Employee employee = Employee.builder().id(1L).firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();

		ResultActions response = mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));

		response.andExpect(status().isCreated()).andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail()))).andDo(print());
	}
	
	@Test
	public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
		// given - precondition or setup

		List<Employee> listOfEmployees = new ArrayList<>();
		listOfEmployees.add(Employee.builder().firstName("Gabriel").lastName("Flamenbaum").email("gflamen@mailnator").build());
		listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@mailnator").build());
		
		employeeRepository.saveAll(listOfEmployees);
		// when - action or the behavior that we are going to test

		ResultActions response = mockMvc.perform(get("/api/employees"));
		// then - verify the input

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
	}
	
	@Test
	public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
		// given - precondition or setup

		Employee employee = Employee.builder().firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();

		employeeRepository.save(employee);

		// when - action or the behavior that we are going to test

		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

		// then - verify the input

		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}
	
	@Test
	public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
		
		Employee employee = Employee.builder().firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();
		employeeRepository.save(employee);

		// when - action or the behavior that we are going to test

		ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1));

		// then - verify the input

		response.andExpect(status().isNotFound()).andDo(print());
	}
	
	@Test
	public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
		// given - precondition or setup

		Employee savedEmployee = Employee.builder().firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();
		
		Employee updatedEmployee = Employee.builder().firstName("Julia").lastName("Prates")
				.email("jup@mailnator.com").build();
		
		employeeRepository.save(savedEmployee);

		// when - action or the behavior that we are going to test
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
													.contentType(MediaType.APPLICATION_JSON)
													.content(objectMapper.writeValueAsString(updatedEmployee)));

		// then - verify the input
		
		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
				.andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
	}
	
	@Test
	public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnReturn404() throws Exception {
		// given - precondition or setup
		
		Employee savedEmployee = Employee.builder().id(1L).firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();
		
		Employee updatedEmployee = Employee.builder().firstName("Julia").lastName("Prates")
				.email("jup@mailnator.com").build();
		
		employeeRepository.save(savedEmployee);
		

		// when - action or the behavior that we are going to test
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1)
													.contentType(MediaType.APPLICATION_JSON)
													.content(objectMapper.writeValueAsString(updatedEmployee)));

		// then - verify the input
		
		response.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@Test
	public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
		// given - precondition or setup

		Employee savedEmployee = Employee.builder().firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();
		
		employeeRepository.save(savedEmployee);
		
		// when - action or the behavior that we are going to test
		ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));
		
		// then - verify the input
		
		response.andExpect(status().isOk());
	}
}
