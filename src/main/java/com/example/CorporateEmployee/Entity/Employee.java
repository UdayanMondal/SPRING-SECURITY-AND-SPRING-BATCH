package com.example.CorporateEmployee.Entity;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.opencsv.bean.CsvBindByName;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Component
@Entity
@Table(name = "emp", schema = "emp")
@SequenceGenerator(name = "custom_sequence_generator", sequenceName = "your_custom_sequence", initialValue = 2000, allocationSize = 1)

public class Employee extends BaseEntity<String> implements Serializable {

	/**
	 * udayan mondal
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_sequence_generator")

	private Long employeeId;

	@NotBlank
	@Column(name = "empName", nullable = false, length = 100)
	@CsvBindByName(column = "name")
	private String name;

	@NotBlank
	@Column(name = "department", nullable = false, length = 100)
	@CsvBindByName(column = "department")
	private String department;

	@Column(name = "address", nullable = false, length = 100)
	@CsvBindByName(column = "address")
	private String address;

	@Column(name = "dob", nullable = true, length = 100)
	@CsvBindByName(column = "dob")
	private String dob;

	@Column(unique = true, nullable = false)
	@Email
	@NotBlank
	@CsvBindByName(column = "email")
	private String email;

	@Column(name = "employeerRole", nullable = false, length = 100)
	@CsvBindByName(column = "employeerRole")
	private String employeerRole;

	@Column(name = "salary", nullable = true, length = 100)
	private Integer salary;

	@CsvBindByName(column = "employerName")
	private String employerName;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeerRole() {
		return employeerRole;
	}

	public void setEmployeerRole(String employeerRole) {
		this.employeerRole = employeerRole;
	}
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "employeeId")
	private EmployeeDetail employeeDetail;

	public EmployeeDetail getEmployeeDetail() {
		return employeeDetail;
	}

	public void setEmployeeDetail(EmployeeDetail employeeDetail) {
		this.employeeDetail = employeeDetail;
	}
	
	
}
