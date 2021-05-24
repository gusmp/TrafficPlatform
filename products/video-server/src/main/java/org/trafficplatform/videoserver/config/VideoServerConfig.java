package org.trafficplatform.videoserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trafficplatform.videoserver.bean.VideoSourcePool;
import org.trafficplatform.videoserver.service.VideoSourcePoolService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class VideoServerConfig {
	
	@Autowired
	private VideoSourcePoolService videoSourcePoolService;
	
	@Bean
	public VideoSourcePool startVideoCapture() {
		
		log.debug("Setting up video source pool...");
		return videoSourcePoolService.reload();
	}
}
