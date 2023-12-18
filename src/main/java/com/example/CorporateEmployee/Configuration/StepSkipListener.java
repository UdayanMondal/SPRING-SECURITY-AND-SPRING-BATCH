package com.example.CorporateEmployee.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

import com.example.CorporateEmployee.Entity.Employer;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public class StepSkipListener  implements SkipListener<Employer, Employer>{
	
	  Logger logger = LoggerFactory.getLogger(StepSkipListener.class);

	    @Override // item reader
	    public void onSkipInRead(Throwable throwable) {
	        logger.info("A failure on read {} ", throwable.getMessage());
	    }

	    public void onSkipInWrite(Number item, Throwable throwable) {
	        logger.info("A failure on write {} , {}", throwable.getMessage(), item);
	    }
// @SneakyThrows allows you to throw a checked exception without declaring it in the method's signature. 
	    @SneakyThrows
	    @Override // item processor
	    public void onSkipInProcess(Employer employer, Throwable throwable) {
	        logger.info("Item {}  was skipped due to the exception  {}", new ObjectMapper().writeValueAsString(employer),
	                throwable.getMessage());
	    }

}

