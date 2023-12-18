package com.example.CorporateEmployee.Configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;


@Component
@EnableAutoConfiguration

public class EmailConfiguration {

	

	@Value("${spring.mail.username}")
	private String username;

	
	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.port}")
	private int port;
	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean auth;

	@Value("${spring.mail.debug}")
	private boolean debug;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean startlsEnable;

	
	
	@Bean
	public JavaMailSender getJavaMailSender()
	{
		JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
		
	
		javaMailSender.setHost(this.host);
		javaMailSender.setPassword(this.password);
		javaMailSender.setUsername(this.username);
		javaMailSender.setPort(this.port);
		Properties props = javaMailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", this.auth);
		props.put("mail.smtp.starttls.enable", this.startlsEnable);
		props.put("mail.debug", this.debug);
		
		
		return javaMailSender;
		
	}
	
}