package com.example.CorporateEmployee.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CorporateEmployee.Entity.Employee;





@Repository
public interface EmployeeUserRepository  extends JpaRepository<Employee, Long>{

}


