package org.trafficplatform.mosaicserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.trafficplatform.opencvlib.boot.OpenCvSetup;

@SpringBootApplication
@ComponentScan(basePackages = "org.trafficplatform.mosaicserver")
@EnableJpaRepositories(basePackages="org.trafficplatform.mosaicserver.repository")
@EntityScan(basePackages="org.trafficplatform.mosaicserver.entity")
public class MosaicServerApplication {

	private static boolean nativeOpenCvLibLoaded = false;
	
	public static void main(String[] args) {
		SpringApplication.run(MosaicServerApplication.class, args);
	}
	
	static {
		
		nativeOpenCvLibLoaded = OpenCvSetup.setup(nativeOpenCvLibLoaded);
	}

}
