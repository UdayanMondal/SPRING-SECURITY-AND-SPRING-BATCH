package com.example.CorporateEmployee.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.CorporateEmployee.Entity.JwtRequest;
import com.example.CorporateEmployee.Entity.JwtResponse;
import com.example.CorporateEmployee.Service.JwtService;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping({"/authenticate"})
    public String  createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        JwtResponse response = jwtService.createJwtToken(jwtRequest);
        return response.getJwtToken(); 
    }
    
}
