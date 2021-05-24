package org.trafficplatform.mosaicserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trafficplatform.mosaicserver.bean.MosaicInputSourcePool;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MosaicServerConfig {
	
	@Bean
	public MosaicInputSourcePool MosaicInputSourcePool() {
		
		log.debug("Setting up mosaic iput source pool...");
		return new MosaicInputSourcePool();
	}
}
