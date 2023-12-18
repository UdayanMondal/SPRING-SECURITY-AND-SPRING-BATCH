package com.example.CorporateEmployee.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import com.example.CorporateEmployee.Entity.JwtRequest;
import com.example.CorporateEmployee.Entity.JwtResponse;
import com.example.CorporateEmployee.Repository.UserRepository;
import com.example.CorporateEmployee.Util.JwtUtil;
import com.example.CorporateEmployee.Entity.User;
import com.example.CorporateEmployee.Exception.BadUserLoginDetailsException;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {

	Logger logger = LoggerFactory.getLogger(JwtService.class);

	@Autowired
	private JwtUtil jwtUtil;
	
	@Value("${user.allowed}")
	private String userEmail;
	
	@Autowired
	private UserRepository userDao;

	@Autowired
	private AuthenticationManager authenticationManager;

	public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
		//username =email
		String userName = jwtRequest.getUserName();
		String userPassword = jwtRequest.getUserPassword();
		authenticate(userName, userPassword);

		UserDetails userDetails = loadUserByUsername(userName);
		String newGeneratedToken = jwtUtil.generateToken(userDetails);

		logger.info("1");
		User user = userDao.findByuserName(userName).get();
		return new JwtResponse(user, newGeneratedToken);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username != null && username.equals(userEmail)) {
			logger.info("3");
			User user = userDao.findByuserName(username).get();
			try {
				return new org.springframework.security.core.userdetails.User(user.getUserName(),
						user.getUserPassword(), getAuthority(user));
			} 
			
			catch (BadCredentialsException e)
			{
				throw new BadUserLoginDetailsException();

			}
			catch (Exception e )
			{
				e.printStackTrace();
				return null;
			}
			
			
		}

		else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

	@SuppressWarnings("rawtypes")
	private Set getAuthority(User user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRole().forEach(role -> {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
		});

		return authorities;
	}

	private void authenticate(String userName, String userPassword) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword)); 
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new BadUserLoginDetailsException(); 
		}
	}

}
