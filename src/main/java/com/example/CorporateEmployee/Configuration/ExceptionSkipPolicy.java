package com.example.CorporateEmployee.Configuration;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

//@Slf4j
public class ExceptionSkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable throwable, long skipCount) throws SkipLimitExceededException {
		// TODO Auto-generated method stub
		if (throwable instanceof NumberFormatException) {
			return true;
		} 
		if (throwable instanceof Exception) {
			return true;
		} 
		
			return false;
	}

}