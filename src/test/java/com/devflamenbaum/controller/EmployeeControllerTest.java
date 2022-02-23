package com.devflamenbaum.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;

import com.devflamenbaum.model.Employee;
import com.devflamenbaum.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest
public class EmployeeControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
		
		Employee employee = Employee.builder()
				.id(1L)
				.firstName("Gabriel")
				.lastName("Flamenbaum")
				.email("gflamen@mailnator.com")
				.build();
		
		given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
					.willAnswer((invocation)-> invocation.getArgument(0));
		
		ResultActions response = mockMvc.perform(post("/api/employees")
													.contentType(MediaType.APPLICATION_JSON)
													.content(objectMapper.writeValueAsString(employee)));
		
		response.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
		// given - precondition or setup
		
		List<Employee> listOfEmployees = new ArrayList<>();
		listOfEmployees.add(Employee.builder().firstName("Gabriel").lastName("Flamenbaum").email("gflamen@mailnator").build());
		listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@mailnator").build());
		given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
		// when - action or the behavior that we are going to test
		
		ResultActions response = mockMvc.perform(get("/api/employees"));
		// then - verify the input
		
		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(listOfEmployees.size())));
	}
}
