package com.example.CorporateEmployee.Processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.CorporateEmployee.Entity.Employer;

public class EmployerProcessor  implements ItemProcessor<Employer, Employer> {

	@Override
	public Employer process(Employer item) throws Exception {
		// TODO Auto-generated method stub
		return item;
	}

}
