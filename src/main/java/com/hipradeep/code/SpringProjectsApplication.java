package com.hipradeep.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringProjectsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectsApplication.class, args);
	}

//	public static void main(String[] args) {
//		PasswordEncoder encoder = new BCryptPasswordEncoder();
//		String rawPassword = "password";
//		String encodedPassword = encoder.encode(rawPassword);
//		System.out.println("Encoded password: " + encodedPassword);
//		System.out.println("Match test: " + encoder.matches(rawPassword, encodedPassword));
//	}

}
