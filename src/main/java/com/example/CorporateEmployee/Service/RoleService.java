package com.example.CorporateEmployee.Service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.CorporateEmployee.Entity.Role;
import com.example.CorporateEmployee.Repository.RoleRepository;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleDao;

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }
}
