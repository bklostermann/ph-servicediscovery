package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@RestController
public class MyController {
	
	@Autowired
	private EurekaClient client;
	
	@Autowired
	private MyService myService;
	
	@Bean
	public RestTemplate rest(RestTemplateBuilder builder) {
		return builder.setConnectTimeout(1000).setReadTimeout(1000).build();
	}
	
	@RequestMapping("/a")
	public String callServiceA(@RequestParam(value = "useProxy", defaultValue="0") boolean useProxy) {
		
		if(useProxy) {
			return myService.callServiceAWithHystrix();
		}
		
		return myService.callServiceA();
	}
	
	@RequestMapping("/b")
	public String callServiceB() {
		InstanceInfo instanceInfo = client.getNextServerFromEureka("service-b", false);
		String baseUrl = instanceInfo.getHomePageUrl();
		
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(baseUrl, String.class);
	}
}
