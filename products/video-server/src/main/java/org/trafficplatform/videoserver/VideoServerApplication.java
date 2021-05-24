package org.trafficplatform.videoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.trafficplatform.opencvlib.boot.OpenCvSetup;


@SpringBootApplication
@ComponentScan(basePackages = "org.trafficplatform.videoserver")
@EnableJpaRepositories(basePackages="org.trafficplatform.videoserver.repository")
@EntityScan(basePackages="org.trafficplatform.videoserver.entity")
public class VideoServerApplication {
	
	
	private static boolean nativeOpenCvLibLoaded = false;

	public static void main(String[] args) {
		SpringApplication.run(VideoServerApplication.class, args);
	}
	
	
	static {
		
		nativeOpenCvLibLoaded = OpenCvSetup.setup(nativeOpenCvLibLoaded);
	}
}