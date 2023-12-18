package com.example.CorporateEmployee.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.CorporateEmployee.Configuration.EmptyFileCheckListener;
import com.example.CorporateEmployee.Configuration.ItemCountListener;
import com.example.CorporateEmployee.Entity.Employee;
import com.example.CorporateEmployee.Entity.EmployeeDetail;
import com.example.CorporateEmployee.Entity.EmployeeInputEntity;
import com.example.CorporateEmployee.Entity.Employer;
import com.example.CorporateEmployee.Exception.CustomInvalidFileFormatException;
import com.example.CorporateEmployee.Repository.EmployerRepository;
import com.example.CorporateEmployee.Service.EmployeeDetailService;
import com.example.CorporateEmployee.Service.EmployeeService;
import com.example.CorporateEmployee.Service.TwilioService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.*;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@Qualifier

public class EmployeeController {
	@Autowired

	private EmployeeService employeeService;
	@Autowired
	private EmployeeDetailService employeeDetailService;
	@Value("${employer.name}")
	private String data;
	@Autowired
	private TwilioService twilioService;
	@Autowired
	private HttpServletRequest request;

	@GetMapping("/public/employee/detail")
	public ResponseEntity<List<Employee>> getAllEmployeeDetails() {

		List<Employee> empDtl = employeeService.getallUserDetail();

		int count = empDtl.size();
		String noOfRecord = String.valueOf(count);
		return new ResponseEntity<List<Employee>>(empDtl, HttpStatus.OK);

	}

	@GetMapping("/private/employee/fetch/{id}")
	//@RolesAllowed("ROLE_EMPLOYEE")
	public ResponseEntity<String> getEmployeeDetailbyId(@PathVariable(value = "id") Long id,
			@RequestParam String phoneNumber) {
		Employee emp=null;
		boolean failure=false;
		try {
			 emp = employeeService.findEmployeeById(id);
			 
		}

		catch (Exception e) {
			e.printStackTrace();
			failure=true;
			return new ResponseEntity<String>(" Record is Found and  CSV File Generated and No Email  sent ",
					HttpStatus.BAD_REQUEST);
			
		}
		
		
		if (emp != null) {
			try {
				twilioService.sendSms(phoneNumber);
				
					return new ResponseEntity<String>(
							"Record is Found and CSV is generated And Email Notification and SMS sent Successfully  ",
							HttpStatus.OK);
				
			}

			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(" Record is Found and  CSV File Generated and No sms sent ",
						HttpStatus.BAD_REQUEST);
			 }
			
		} else

			return new ResponseEntity<String>("No Record is Found and No CSV File Generated and No sms sent ",
					HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/public/employee/create")
	public ResponseEntity<String> createEmployeeRecord(@RequestBody EmployeeInputEntity employee) throws Exception {
		Boolean employeeCreated;
		try {

			String empName = data;
			employee.setEmployerName(empName);
			employeeCreated = employeeService.createEmployeeWithDetail(employee);
			if (employeeCreated) {

				return new ResponseEntity<String>("Created Successfully", HttpStatus.CREATED);

			} else
				return new ResponseEntity<String>("Not create successfully."+ " "+ "Please see the console for exception Track Trace" , HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);

		}

	}

	@PutMapping("/private/employee/update/{id}")
	public ResponseEntity<String> updateEmployeeRecord(@PathVariable(value = "id") Long id,
			@RequestBody EmployeeInputEntity input) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// Map EmployeeInputEntity to employee
		Employee employee = objectMapper.convertValue(input, Employee.class);
		employee.setEmployeeId(id);
		
		try {
			Employee updatedEmployee = employeeService.UpdateEmployeeById(employee);
			
			return new ResponseEntity<String>("Successfully updated in the Employee table", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();

			return new ResponseEntity<String>("Not Successfully updated ", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/private/employee/delete/{id}")
	@RolesAllowed({ "ROLE_EMPLOYEE", "ROLE_ADMIN" })
	public ResponseEntity<String> updateEmployeeRecord(@PathVariable(value = "id") Long id) {
try {
		this.employeeService.deleteEmployeeById(id);
		return new ResponseEntity<String>("User successfully deleted!", HttpStatus.OK);
}
catch (Exception e)
{
	return new ResponseEntity<String>("User Not successfully deleted!" + " "+e.getCause().toString(), HttpStatus.BAD_REQUEST);

}
		
	}

	@GetMapping("/private/send/sms")
	public String sendSms(@RequestParam String phoneNumber) {
		try {
			twilioService.sendSms(phoneNumber);
			return "SMS sent successfully!";
		}

		catch (Exception e) {
			System.out.println(e);
			return "SMS  Not sent successfully!";
		}
	}

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Autowired
	private EmployerRepository repository;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private EmptyFileCheckListener emptyFileCheckListener;
	private final String TEMP_STORAGE = "C:/Users/Rikim/Downloads/Corporate-Employee/src/test/java/com/example/CorporateEmployee/";

	@SuppressWarnings("unchecked")
	@PostMapping(path = "public/importData")
	public String startBatchCSVFileToMysql(@RequestParam("file") MultipartFile multipartFile) {
		HttpSession session = request.getSession();
		// file -> path we don't know
		// copy the file to some storage in your VM : get the file path
		// copy the file to DB : get the file path
		String originalFileName = multipartFile.getOriginalFilename();
		String extension = null;
		int index = originalFileName.lastIndexOf('.');
		if (index > 0) {
			extension = originalFileName.substring(index + 1);

		}
		if (extension != null && !extension.equals("csv")) {
			throw new CustomInvalidFileFormatException("The input file is not in the standard expected CSV format ");
		}
		try {

			File fileToImport = new File(TEMP_STORAGE + originalFileName);
			// multipartFile.transferTo(fileToImport);

			JobParameters jobParameters = new JobParametersBuilder()
					.addString("fullPathFileName", TEMP_STORAGE + originalFileName)
					.addLong("startAt", System.currentTimeMillis()).toJobParameters();

			JobExecution execution = jobLauncher.run(job, jobParameters);
			// Check if the CSV file is empty
			int itemCount = 0;
			if (session != null) {
				session.invalidate();
				itemCount = emptyFileCheckListener.getItemCount();
			}

			if (itemCount == 0) {
				return "The CSV file is empty ,Batch process so No record inserted ";
			} else {
				if (execution.getExitStatus().getExitCode().equals("COMPLETED")) {

					// delete the file from the TEMP_STORAGE
					// Files.deleteIfExists(Paths.get(TEMP_STORAGE+ originalFileName));
					return "CSVFileToMysql Successfully transferred with  total records " + String.valueOf(itemCount) + " "
							+ "  in the database table  ";
				} else if (execution.getExitStatus().getExitCode().equals("FAILED")) {
					return "CSVFileToMysql Process Filed ";
				} else
					return "CSVFileToMysql Process Completed ";
			}
		}

		catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {

			e.printStackTrace();
			return "CSVFileToMysql UnSuccessful Due to " + e.getCause();
		}

	}

}
