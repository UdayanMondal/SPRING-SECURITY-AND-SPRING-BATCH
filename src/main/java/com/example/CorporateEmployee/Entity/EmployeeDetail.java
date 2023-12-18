package com.example.CorporateEmployee.Entity;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Component

@Table(name = "empDtl", schema = "emp")
@SequenceGenerator(name = "custom_sequence_generator", sequenceName = "your_custom_sequence", initialValue = 5000, allocationSize = 1)
public class EmployeeDetail extends BaseEntity<String> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_sequence_generator")
	private Long employeeDetailId;

	@Column(name = "employeeId",insertable=false, updatable=false)
	private Long employeeId;

	
	@Column(name = "panNumber", nullable = true, length = 100)

	private String panNumber;
	
	@Column(name = "adhaarNumber", nullable = true, length = 100)
	private String adhaarNumber;

	
	@Column(name = "verifiedEmployer", nullable = false, length = 100)
	private boolean verifiedEmployer;

	// One UserDetail belongs to one User
	@OneToOne
	@JoinColumn(name = "employeeId")
	private Employee employee;

}
