package com.demo.codetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CodeTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeTestApplication.class, args);
	}

}
