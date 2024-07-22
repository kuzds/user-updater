package com.example.user.updater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.Ordered;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry(order = Ordered.LOWEST_PRECEDENCE - 4)
public class UserUpdaterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserUpdaterApplication.class, args);
	}

}
