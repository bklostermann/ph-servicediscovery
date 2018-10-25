package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class ServiceAApplication {

	@Value( "${server.instance.name}" )
	private String instance;
	
	public static void main(String[] args) {
		SpringApplication.run(ServiceAApplication.class, args);
	}
	
	@RequestMapping("/")
	public String message() throws Exception {
		// Circuit has a 1000 timeout by default
		Thread.sleep(3000);
		return "Served by " + instance;
	}
}
