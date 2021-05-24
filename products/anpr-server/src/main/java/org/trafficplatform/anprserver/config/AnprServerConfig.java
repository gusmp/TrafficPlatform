package org.trafficplatform.anprserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AnprServerConfig {
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
}
