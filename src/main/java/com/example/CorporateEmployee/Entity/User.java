package com.example.CorporateEmployee.Entity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;

@Entity
@SequenceGenerator(name = "custom_sequence_generator", sequenceName = "your_custom_sequence", initialValue = 2000, allocationSize = 1)
public class User {


     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_sequence_generator")
	private Integer id;
     @Column(nullable = false)
     // username=email 
    @NotBlank
	private String userName;
     @Column(nullable = false)
	private String userFirstName;
     @Column(nullable = false)
	private String userLastName;
     @Column(nullable = false)
	private String userPassword; 
     @Column(nullable = false)
     @Email
 	 @NotBlank
     private String email;
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE) 
	@JoinTable(name = "USER_ROLE" , joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "id") }, inverseJoinColumns = {
			@JoinColumn(name = "ROLE_ID", referencedColumnName = "roleId") })
	private Set<Role> role;

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
