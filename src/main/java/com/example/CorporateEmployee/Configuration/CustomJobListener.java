package com.example.CorporateEmployee.Configuration;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;

public class CustomJobListener implements JobExecutionListener {

	private final HttpSession session;

//constructor injection 
	@Autowired
	public CustomJobListener(HttpSession session) {
		this.session = session;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("Batch job completed. Performing cleanup...");
		if (session != null) {

			// Invalidate the session after job completion
			session.invalidate();
		}
		System.out.println("Session ended after batch job completion.");
	}

}
