package org.trafficplatform.platformui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.trafficplatform.opencvlib.boot.OpenCvSetup;

@SpringBootApplication
@ComponentScan(basePackages = "org.trafficplatform.platformui")
@EnableJpaRepositories(basePackages="org.trafficplatform.platformui.repository")
@EntityScan(basePackages="org.trafficplatform.platformui.entity")
public class PlatformUiApplication {

	private static boolean nativeOpenCvLibLoaded = false;
	
	public static void main(String[] args) {
		SpringApplication.run(PlatformUiApplication.class, args);
	}
	
	static {
		
		nativeOpenCvLibLoaded = OpenCvSetup.setup(nativeOpenCvLibLoaded);
	}

}
