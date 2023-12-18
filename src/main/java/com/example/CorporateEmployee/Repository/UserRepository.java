package com.example.CorporateEmployee.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CorporateEmployee.Entity.User;



@Repository
public interface UserRepository extends JpaRepository<User,Integer > {

	Optional<User> findByuserName(String userName);
	 Boolean existsByUserName(String userName);
	
}
