package com.example.CorporateEmployee.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EMPLOYER_INFO")

public class Employer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Override
	public String toString() {
		return "Employer [ name=" + name + ", address=" + address + ", email=" + email + ", noOfEmployee="
				+ noOfEmployee + ", employerType=" + employerType + ", netWorth=" + netWorth + "]";
	}

	public Employer() {
		super();
	}

	@Column(name = "name", nullable = false, updatable = false, length = 100)
	private String name;

	@Column(name = "address", nullable = false, updatable = false, length = 100)
	private String address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getNoOfEmployee() {
		return noOfEmployee;
	}

	public void setNoOfEmployee(int noOfEmployee) {
		this.noOfEmployee = noOfEmployee;
	}

	public String getEmployerType() {
		return employerType;
	}

	public void setEmployerType(String employerType) {
		this.employerType = employerType;
	}

	public String getNetWorth() {
		return netWorth;
	}

	public void setNetWorth(String netWorth) {
		this.netWorth = netWorth;
	}

	@Column(name = "email", nullable = false, updatable = false, length = 100)
	private String email;
	@Column(name = "noOfEmployee", nullable = false, updatable = false, length = 100)
	private int noOfEmployee;
	@Column(name = "employerType", nullable = false, updatable = false, length = 100)
	private String employerType;
	@Column(name = "netWorth (Cr)", nullable = false, updatable = false, length = 100)
	private String netWorth;

	public Employer(String name, String address, String email, int noOfEmployee, String employerType, String netWorth) {
		super();
		this.name = name;
		this.address = address;
		this.email = email;
		this.noOfEmployee = noOfEmployee;
		this.employerType = employerType;
		this.netWorth = netWorth;
	}
}
