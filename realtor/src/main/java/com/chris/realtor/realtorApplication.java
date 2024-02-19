package com.chris.realtor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.chris.realtor.Processors.UserProcessor;

@SpringBootApplication
public class realtorApplication {

	private final UserProcessor userProcessor;

    @Autowired
    public realtorApplication(UserProcessor userProcessor) {
        this.userProcessor = userProcessor;
    }
	public static void main(String[] args) {
		SpringApplication.run(realtorApplication.class, args);
	}
}
