package com.example.CorporateEmployee.Service;

import java.util.List;

import com.example.CorporateEmployee.Entity.Employee;
import com.example.CorporateEmployee.Entity.EmployeeInputEntity;



public interface UserServiceCommon {
	
	public Boolean createEmployeeWithDetail(EmployeeInputEntity employee) throws Exception ;
	public Employee findEmployeeById( Long id );
	public List<Employee >getallUserDetail();
	public void deleteEmployeeById(Long  userId);
	
}
