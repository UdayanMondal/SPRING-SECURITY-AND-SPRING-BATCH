package com.example.CorporateEmployee.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CorporateEmployee.Entity.EmployeeDetail;
import com.example.CorporateEmployee.Repository.EmployeeUserDetailRepository;
@Service

public class EmployeeDetailService {
	@Autowired
	private EmployeeUserDetailRepository repository;
	
	public List<EmployeeDetail> getallUserDetail() {
		List<EmployeeDetail> emp = repository.findAll();
		return emp;
	}
	public EmployeeDetail findByDetailId(Long id)
	{
		Optional<EmployeeDetail> detail=repository.findById(id);
		if (detail.isPresent())
		{
			return detail.get();
		}
		else 
		{
			return null;
		}
	}
	
	public EmployeeDetail findByempId(Long empId)
	{
		EmployeeDetail detail=repository.findDetailIdByempId(empId);
		return detail;
	}
	public void  deleteByempId(Long empId)
	{
   repository.deleteById(empId);
		
	}
	
	
	
	
}
