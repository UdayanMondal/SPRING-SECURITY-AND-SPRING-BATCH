package com.example.CorporateEmployee.Service;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.CorporateEmployee.Entity.Role;
import com.example.CorporateEmployee.Entity.User;
import com.example.CorporateEmployee.Repository.RoleRepository;
import com.example.CorporateEmployee.Repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashSet;   
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
@Service
public class UserService {

	@Autowired
	private UserRepository userDao;

	@Autowired
	private RoleRepository roleDao;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PersistenceContext
	private EntityManager entityManager;

	public void initRoleAndUser() {
		
		userDao.deleteAll();
		roleDao.deleteAll();  
		
		Role adminRole = new Role();
		
		adminRole.setRoleName("ADMIN");
		adminRole.setRoleDescription("Admin role");
		roleDao.save(adminRole);  

		Role userRole = new Role();
		
		userRole.setRoleName("USER");
		userRole.setRoleDescription("Default role for newly created record");
		roleDao.save(userRole);

		Role EmployeeRole = new Role();
		EmployeeRole.setRoleName("EMPLOYER");
		EmployeeRole.setRoleDescription(" Role for newly created record");
		roleDao.save(EmployeeRole);
		// admin user
		User adminUser = new User();
		adminUser.setUserName("admin@gmail.com");
		adminUser.setUserPassword(getEncodedPassword("password"));
		adminUser.setUserFirstName("admin");
		adminUser.setUserLastName("admin");
		adminUser.setEmail("admin@gmail.com");
		Set<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);
		adminUser.setRole(adminRoles);
		userDao.save(adminUser); 
		// employee user
		User employeeUser = new User();
		employeeUser.setUserName("user@gmail.com");
		employeeUser.setUserPassword(getEncodedPassword("password"));
		employeeUser.setUserFirstName("RAM");
		employeeUser.setUserLastName("KRISHNA");
		employeeUser.setEmail("user@gmail.com");
		Set<Role> employeeRoles = new HashSet<>();

		employeeRoles.add(EmployeeRole);
		employeeRoles.add(adminRole);
		employeeUser.setRole(employeeRoles);
		

		
		
		userDao.save(employeeUser);
	}
	
	public String  registerNewUser(User user) {
		java.util.Optional<Role> roleOptional = roleDao.findByroleName("USER");
// Create new user's account

		Set<Role> strRoles = user.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleDao.findByroleName("USER")
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {

			List<Role> arr = strRoles.stream().collect(Collectors.toList());
			if (arr.size() > 0) {
				for (int i = 0; i < arr.size(); i++) {
					String role = arr.get(i).toString();
					switch (role) {
					case "admin":
						Role adminRole = roleDao.findByroleName("ADMIN")
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
						break;
					default:
						Role userRole = roleDao.findByroleName("EMPLOYER")
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
					}

				}
			}

		}
		if (roleOptional.isPresent()) {
			Role role = roleOptional.get();
			/*
			 * Set<Role> userRoles = new HashSet<>(); userRoles.add(role);
			 * user.setRole(userRoles);
			 */
			user.setRole(roles);
			user.setUserPassword(getEncodedPassword(user.getUserPassword()));

			try {
				User saveUserDetails= saveUser(user);
				return "Registered";
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return e.getCause().toString();
			}
			
		} else {
			throw new NoSuchElementException();
			// Handle the case where "User" role is not found
		}

	}

	public String getEncodedPassword(String password) {
		return passwordEncoder.encode(password);
	}
	public User saveUser(User user)
	{
		 return userDao.save(user);
	}

	public Boolean updateUser(User user) {
		boolean isUpdated = false;
		if (userDao.findByuserName(user.getUserName()) != null) {
			saveUser(user);
			isUpdated = true;
		}
		return isUpdated;
	}
	public List<User> getAllUser() {
		return userDao.findAll();
	}
}
