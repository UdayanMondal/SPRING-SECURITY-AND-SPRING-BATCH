package com.example.CorporateEmployee.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CorporateEmployee.Entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer > {

	Optional<Role> findByroleName(String role);

}
