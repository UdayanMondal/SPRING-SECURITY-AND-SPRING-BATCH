package com.example.CorporateEmployee.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.CorporateEmployee.Entity.EmployeeDetail;

@Repository
public interface EmployeeUserDetailRepository extends JpaRepository<EmployeeDetail, Long> {
	  @Query(value = "Select  *  from emp.emp_dtl t where t.employee_id=?1",nativeQuery = true ) 
	  public   EmployeeDetail   findDetailIdByempId(Long  empId);  
}
