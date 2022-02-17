package com.devflamenbaum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devflamenbaum.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	Optional<Employee> findByEmail(String email);
	
	@Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
	Employee findByJPQL(String firstName, String lastName);
	
	@Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
	Employee findByJPQLNamedParams(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	@Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
	Employee findByNativeSQL(String firstName, String lastName);

}
