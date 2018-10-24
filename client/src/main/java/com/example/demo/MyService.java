package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class MyService {

	@Autowired
	private EurekaClient client;
	
	private final RestTemplate restTemplate;
	
	public MyService(RestTemplate rest) {
		restTemplate = rest;
	}
	
	@HystrixCommand(fallbackMethod = "reliable")
	public String call() {
		InstanceInfo instanceInfo = client.getNextServerFromEureka("service-a", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		
		return restTemplate.getForObject(baseUrl, String.class);
	}
	
	public String reliable() {
		return "From reliable()";
	}
}
