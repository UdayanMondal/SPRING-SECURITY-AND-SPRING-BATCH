package com.example.CorporateEmployee.Configuration;

import java.util.List;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CorporateEmployee.Entity.Employer;
import com.example.CorporateEmployee.Repository.EmployerRepository;

@Service
public class EmployerItemWriter implements ItemWriter<Employer> {

	@Autowired
	private EmployerRepository repository;

	@Override
	public void write(Chunk<? extends Employer> chunk) throws Exception {
		System.out.println("Writer Thread " + Thread.currentThread().getName());
		 repository.saveAll(chunk);
	}

}
