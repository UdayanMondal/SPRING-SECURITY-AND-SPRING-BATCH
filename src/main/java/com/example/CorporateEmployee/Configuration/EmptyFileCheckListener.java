package com.example.CorporateEmployee.Configuration;

import org.springframework.batch.core.ItemReadListener;

public class EmptyFileCheckListener <Chunk> implements ItemReadListener<Chunk> {
	 private int itemCount = 0;
	  
	    public void beforeRead() {
	        // Not needed
	    }

	
	    public void afterRead(Chunk item) {
	    	itemCount++;
	    }

	 
	    public void onReadError(Exception ex) {
	    	ex.printStackTrace();
	     
	    }

	    public int getItemCount() {
	        return itemCount;
	    }
	
}
