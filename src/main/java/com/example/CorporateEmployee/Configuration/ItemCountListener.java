package com.example.CorporateEmployee.Configuration;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ItemCountListener implements ChunkListener {

	@Override
	public void beforeChunk(ChunkContext context) {
	}

	@Override
	public void afterChunk(ChunkContext context) {

		Long count = context.getStepContext().getStepExecution().getReadCount();

	}

	public Long getRecordCount(ChunkContext context) {

		Long count = context.getStepContext().getStepExecution().getReadCount();
		return count;
	}

}
