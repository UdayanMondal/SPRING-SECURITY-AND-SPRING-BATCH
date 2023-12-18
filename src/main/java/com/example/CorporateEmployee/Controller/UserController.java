package com.example.CorporateEmployee.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.CorporateEmployee.Entity.User;
import com.example.CorporateEmployee.Repository.UserRepository;
import com.example.CorporateEmployee.Service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userDao;

	@jakarta.annotation.PostConstruct
	public void initRoleAndUser() {
		userService.initRoleAndUser();
	}

	@PostMapping({ "/public/registerNewUser" })
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<String> registerNewUser(@RequestBody User user) {
		if (userDao.existsByUserName(user.getUserName())) { 
			return ResponseEntity.badRequest().body("Error: Username/Email is already taken!");
		} else {
			try {
			String newUser = userService.registerNewUser(user);

			if (newUser.equals("Registered")) {
				return ResponseEntity.ok().body("New User Successfully Created and Registered Successfully");
			} else {
				return ResponseEntity.ok().body("New User Not  Successfully Created and Registered .Please see the console for Exception Tracktrace" + "  "+newUser);
			}
			
			}
			catch (Exception e )
			{
				e.printStackTrace();
				return ResponseEntity.badRequest().body(e.getCause().toString());
			}
		}

	}

	@PutMapping({ "/public/updateUser" })
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> updateUser(@RequestBody User user) {

		if (!userService.updateUser(user)) {
			return ResponseEntity.badRequest().body("User not present !");
		} else {

			return ResponseEntity.ok().body(" User Successfully is updated");
		}

	}

	@GetMapping
	@ResponseStatus(value = HttpStatus.OK)
	public List<User> getAllUser() {

		return userService.getAllUser();

	}

	@GetMapping({ "/forAdmin" })
	@PreAuthorize("hasRole('ADMIN')")
	public String forAdmin() {
		return "This URL is only accessible to the admin";
	}

	@GetMapping({ "/forEmployer" })
	@PreAuthorize("hasRole('EMPLOYEE')")
	public String forEmployee() {
		return "This URL is only accessible to the Employee";
	}
}
