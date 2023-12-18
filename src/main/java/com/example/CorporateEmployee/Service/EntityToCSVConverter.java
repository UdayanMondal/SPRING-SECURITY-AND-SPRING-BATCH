package com.example.CorporateEmployee.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.comparators.FixedOrderComparator;
import org.springframework.stereotype.Service;

import com.example.CorporateEmployee.Entity.Employee;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;



@Service
public class EntityToCSVConverter {

	
	//List of objects into csv file 
@SuppressWarnings("unchecked")
public static void writeToCsv(String filePath, Comparator<String> header, List<Employee> entity) throws IOException ,
	 CsvDataTypeMismatchException,
     CsvRequiredFieldEmptyException {
	
    try (FileWriter writer = new FileWriter(filePath)) {
    	
        @SuppressWarnings("rawtypes")
       
        
		//ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
        
        HeaderColumnNameMappingStrategy<Employee> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(Employee.class);
        mappingStrategy.setColumnOrderOnWrite(header);

        // Write the header row using the custom comparator
       
        StatefulBeanToCsv<Employee> beanToCsv = new StatefulBeanToCsvBuilder<Employee>(writer)
                .withMappingStrategy(mappingStrategy)
                .withSeparator(',')
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .build();
        
        beanToCsv.write(entity);
        writer.close();
        System.out.println("CSV file saved successfully.");
      

    } catch (CsvRequiredFieldEmptyException e) {
        e.printStackTrace();
      
    } catch (CsvDataTypeMismatchException e) {
        e.printStackTrace();
       
    }
    catch (IOException e) {
        e.printStackTrace();
      
    } 
    
   
}


}
