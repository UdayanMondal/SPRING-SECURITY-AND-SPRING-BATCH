package com.example.CorporateEmployee.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CorporateEmployee.Entity.Employer;
@Repository
public interface EmployerRepository  extends JpaRepository <Employer,Long>{

}
