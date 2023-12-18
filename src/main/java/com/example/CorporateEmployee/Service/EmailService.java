package com.example.CorporateEmployee.Service;



import java.io.File;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
@Service
public class EmailService {
	@Value("${spring.mail.username}")
	private String senderEmail;
 
 
	
	 @Autowired private JavaMailSender javaMailSender;
	 
	 
	 
	    public void sendEmailWithAttachment(String to, String subject, String text, String filePath,String body) throws MessagingException {
	    	jakarta.mail.internet.MimeMessage createMimeMessage = javaMailSender.createMimeMessage(); 
			MimeMessageHelper helper = new MimeMessageHelper(createMimeMessage, true, CharEncoding.UTF_8);
			//InputStream is = new ByteArrayInputStream(baos.toByteArray());
			//message.addAttachment("facture.pdf",  new ByteArrayResource(IOUtils.toByteArray(is)))
			helper.setFrom(senderEmail);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			

	        // Attach the file
	        FileSystemResource file = new FileSystemResource(new File(filePath));
	        helper.addAttachment("AttachmentName.csv", file);

	        javaMailSender.send(createMimeMessage);
			System.out.println("Mail Sent Successfully");
	    }
	    
	    

	       


}
