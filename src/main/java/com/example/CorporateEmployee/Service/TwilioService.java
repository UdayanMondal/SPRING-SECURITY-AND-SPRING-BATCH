package com.example.CorporateEmployee.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

@Service
public class TwilioService {

	@Value("${twilio.accountSid}")
    private String accountSid;
    
    @Value("${twilio.authToken}")
    private String authToken;
    
    @Value("${twilio.phoneNumber}")
    private String phNumber;
    
    public void sendSms(String recepeintNumber) throws Exception  {
    	String message ="Employee CSV file successfully generated !";
       Twilio.init(accountSid, authToken);
        String fromPhoneNumber = phNumber;// Your Twilio phone number
        String to="+91"+recepeintNumber;
        try {
        Message.creator(
            new com.twilio.type.PhoneNumber(to),
            new com.twilio.type.PhoneNumber(fromPhoneNumber),
            message
        ).create();
        
        }
        catch (Exception e)
        {
        	System.out.println(e.getCause() +e.getMessage());
        }
    }
}
