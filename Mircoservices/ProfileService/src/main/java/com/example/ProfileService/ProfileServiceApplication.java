package com.example.ProfileService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RestController x
public class ProfileServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileServiceApplication.class, args);
	}

}

@GetMapping("/hello")
public String hello(
		@Requestparam(value = "name", defaultValue = "World") String name)
{
	return String.format("Hello %s!", name);
}