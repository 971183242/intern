package com.oocl.workshop.intern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class InternApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternApplication.class, args);
	}

}
