package com.example.CorporateEmployee.Entity;

import java.sql.Timestamp;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  //component will keep track of changes 

//Auditing support by spring data jpa for audit related column 
public abstract class BaseEntity <T> {
@CreatedDate
@Column(updatable=false)
	private Timestamp  createdOn;
@CreatedBy
@Column(updatable=false)
	private String createdBy;
@LastModifiedDate
@Column(insertable=false)
	private Timestamp updatedOn;
	@LastModifiedBy
	@Column(insertable=false)
	private String UpdatedBy;
	
}
