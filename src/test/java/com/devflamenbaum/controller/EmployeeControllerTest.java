package com.devflamenbaum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.devflamenbaum.model.Employee;
import com.devflamenbaum.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.ArgumentMatchers.any;

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

		Employee employee = Employee.builder().id(1L).firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();

		given(employeeService.saveEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

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
		listOfEmployees
				.add(Employee.builder().firstName("Gabriel").lastName("Flamenbaum").email("gflamen@mailnator").build());
		listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@mailnator").build());
		given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
		// when - action or the behavior that we are going to test

		ResultActions response = mockMvc.perform(get("/api/employees"));
		// then - verify the input

		response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
	}

	@Test
	public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
		// given - precondition or setup
		long employeeId = 1L;

		Employee employee = Employee.builder().id(1L).firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();

		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

		// when - action or the behavior that we are going to test

		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

		// then - verify the input

		response.andExpect(status().isOk()).andDo(print())
				.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
				.andExpect(jsonPath("$.email", is(employee.getEmail())));
	}

	@Test
	public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
		
		long employeeId = 1L;
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

		// when - action or the behavior that we are going to test

		ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

		// then - verify the input

		response.andExpect(status().isNotFound()).andDo(print());
	}
	
	@Test
	public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
		// given - precondition or setup
		long employeeId = 1L;

		Employee savedEmployee = Employee.builder().id(1L).firstName("Gabriel").lastName("Flamenbaum")
				.email("gflamen@mailnator.com").build();
		
		Employee updatedEmployee = Employee.builder().firstName("Julia").lastName("Prates")
				.email("jup@mailnator.com").build();
		
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
		given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

		// when - action or the behavior that we are going to test
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
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
		long employeeId = 1L;

		
		Employee updatedEmployee = Employee.builder().firstName("Julia").lastName("Prates")
				.email("jup@mailnator.com").build();
		
		given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
		given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));

		// when - action or the behavior that we are going to test
		ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
													.contentType(MediaType.APPLICATION_JSON)
													.content(objectMapper.writeValueAsString(updatedEmployee)));

		// then - verify the input
		
		response.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@Test
	public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
		// given - precondition or setup
		long employeeId = 1L;
		
		willDoNothing().given(employeeService).deleteEmployee(employeeId);
		// when - action or the behavior that we are going to test
		ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
		
		// then - verify the input
		
		response.andExpect(status().isOk());
	}
}
