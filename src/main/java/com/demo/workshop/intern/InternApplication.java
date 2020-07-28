package com.demo.workshop.intern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InternApplication  {
	public static void main(String[] args) {
		SpringApplication.run(InternApplication.class, args);
	}

}
