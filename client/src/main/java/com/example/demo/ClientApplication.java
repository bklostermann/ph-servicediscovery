package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@RestController
public class ClientApplication {

	@Autowired
	private EurekaClient client;
	
	@Autowired
	private MyService myService;
	
	@Bean
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
	
	@RequestMapping("/")
	public String callService() {
		InstanceInfo instanceInfo = client.getNextServerFromEureka("service-a", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(baseUrl, String.class);
	}
	
	@RequestMapping("/hystrix")
	public String callService3() {
		return myService.call();
	}
	
	@RequestMapping("/fromnode")
	public String callService2() {
		InstanceInfo instanceInfo = client.getNextServerFromEureka("node-service", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(baseUrl, String.class);
	}
}
