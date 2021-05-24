package org.trafficplatform.anprserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.trafficplatform.opencvlib.boot.OpenCvSetup;

@SpringBootApplication
@ComponentScan(basePackages = "org.trafficplatform.anprserver")
@EnableJpaRepositories(basePackages="org.trafficplatform.anprserver.repository")
@EntityScan(basePackages="org.trafficplatform.anprserver.entity")
public class AnprServerApplication {

	//private static boolean nativeOpenCvLibLoaded = false;
	
	public static void main(String[] args) {
		SpringApplication.run(AnprServerApplication.class, args);
	}
	
	static {
		
		//nativeOpenCvLibLoaded = OpenCvSetup.setup(nativeOpenCvLibLoaded);
	}

}
