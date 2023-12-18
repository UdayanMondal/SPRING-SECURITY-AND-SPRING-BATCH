package com.example.CorporateEmployee.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.stereotype.Service;

import com.example.CorporateEmployee.Entity.Employee;
import com.example.CorporateEmployee.Entity.EmployeeDetail;
import com.example.CorporateEmployee.Entity.EmployeeInputEntity;
import com.example.CorporateEmployee.Entity.Role;
import com.example.CorporateEmployee.Exception.DateFormatException;
import com.example.CorporateEmployee.Exception.ResourceNotFoundException;
import com.example.CorporateEmployee.Repository.EmployeeUserDetailRepository;
import com.example.CorporateEmployee.Repository.EmployeeUserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.example.CorporateEmployee.Entity.CustomHeaderComparator;

@Service
public class EmployeeService implements UserServiceCommon {
	@Autowired
	private EmployeeUserRepository repository;
	@Autowired
	private EmployeeDetailService employeeUserDetail;
	@Value("${employer.name}")
	private String employer;
	@Value("${file.Location}")
	private String CSV_filepath;
	@Autowired
	private EmailService emailService;
	@Autowired
	private EntityToCSVConverter entityToCSVConverter;
	@Value("${receiverMailAddress}")
	private String receiverMailAddress;
	public List<Employee> getallUserDetail() {
		List<Employee> emp = repository.findAll();
		return emp;
	}

	public Boolean createEmployeeWithDetail(EmployeeInputEntity input) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// Map EmployeeInputEntity to employee
		Employee employee = objectMapper.convertValue(input, Employee.class);
		boolean success = false;
		Boolean validDate = isValidDate(employee.getDob());
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		// putting value to Employee Entity
		EmployeeDetail employeeDetail = new EmployeeDetail();

		if (input.getAdhaarNumber() != null || input.getPanNumber() != null) {
			employeeDetail.setVerifiedEmployer(true);
		} else {
			employeeDetail.setVerifiedEmployer(false);
		}
		employeeDetail.setEmployee(employee);
		employeeDetail.setAdhaarNumber(input.getAdhaarNumber());
		employeeDetail.setPanNumber(input.getPanNumber());
		employeeDetail.setCreatedBy("SYSTEM");
		employeeDetail.setCreatedOn(timeStamp);
		employee.setEmployeeDetail(employeeDetail);
		employee.setCreatedBy("SYSTEM");
		
		employee.setCreatedOn(timeStamp);
		try {

			Employee emp = repository.save(employee);
			if (emp.getEmployeeId() != null) {
				// Record inserted successfully, and the primary key has been set
				success = true;
			} else {

				// Insert failed
				success = false;
			}
		}

		catch (DataIntegrityViolationException e) {
			// Insertion failed due to a data integrity violation (e.g., unique constraint
			// violation)
			e.printStackTrace();
			

		} catch (Exception e) {
			e.printStackTrace();

		}

		return success;
	}

	public Employee findEmployeeById(Long id) {

		Optional<Employee> optionalUser = repository.findById(id);

		if (!optionalUser.isPresent()) {
			throw new ResourceNotFoundException("Employee", "ID", id);
		}

		Employee emp = optionalUser.get();
		if (emp != null) {
			try {
				List<Employee> empList = new ArrayList();
				empList.add(emp);
				String generatedFileName = generateCsv(empList);
				this.sendNotification(generatedFileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return emp;
	}

	public Employee UpdateEmployeeById(Employee employee) throws Exception {

		Long empId = employee.getEmployeeId();
		Employee emp = this.repository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", empId));
    //check date format 
		Boolean validDate = isValidDate(employee.getDob());

		Employee updateEmployee = this.repository.save(employee);

		return updateEmployee;
	}

	@Override
	public void deleteEmployeeById(Long empId) {
		Employee emp = this.repository.findById(empId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "ID", empId));
		
		EmployeeDetail detail =this.employeeUserDetail.findByempId(empId);
		Long detailId=detail.getEmployeeDetailId();
		if (detailId != null) {
			this.employeeUserDetail.deleteByempId(detailId);
			this.repository.deleteById(empId);
		}
		
	}

	public static boolean isValidDate(String inDate) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(inDate.trim());
		} catch (ParseException e) {
			// Handle the exception
			e.printStackTrace();
			throw new DateFormatException(inDate, e);

		}
		return true;
	}

	public String generateCsv(List<Employee> empDtl) throws IOException {

		// Define the desired column order
		String[] desiredOrder = { "name", "department", "employerName", "role", "dob", "email", "address" };

		// Create a custom header comparator
		Comparator<String> headerComparator = new CustomHeaderComparator(desiredOrder);

		// generate unique file name
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerValue = "Employee_" + currentDateTime + ".csv";

		String filepath = CSV_filepath + headerValue;

		try {
			entityToCSVConverter.writeToCsv(filepath, headerComparator, empDtl);
			return headerValue;
		} catch (CsvDataTypeMismatchException e) {

			e.printStackTrace();
			return null;
		} catch (CsvRequiredFieldEmptyException e) {

			e.printStackTrace();
			return null;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}

	}

	public String sendNotification(String generatedFileName) {
		
		String to =receiverMailAddress;
		String subject = "Employee Data";
		String text = "This is the email body content.";
		String body = this.readRegEmailBody(generatedFileName);
		try {
			String filePath = CSV_filepath + generatedFileName;
			emailService.sendEmailWithAttachment(to, subject, text, filePath, body);
			return "Notification sent successfully!";
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Email Notification Not sent successfully!");
			return "Notification Not sent successfully!";
		}

	}

	private String readRegEmailBody(String generatedFileName) {

		String url = "";
		String fileName = "EmployeeData-successful-email.txt";
		String mailBody=null;
		try {
			String filePath = CSV_filepath + generatedFileName;
			File file = new File(filePath);

			FileReader fr = new FileReader(file);
			BufferedReader bf = new BufferedReader(fr);

			StringBuffer sb = new StringBuffer();

			String line = bf.readLine();
			while (line != null) {
				sb.append(line);
				line = bf.readLine();
			}
			bf.close();

			mailBody = sb.toString();
			 mailBody=mailBody.replace("{FULLNAME}", "USER");
			 mailBody=mailBody.replace("{EMPLOYER-NAME}", employer+"");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mailBody;
	}
}
