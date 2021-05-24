package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnprCloudRoot {
	
	private AnprCloudData data;
	private String node;
	private int nodetime;
	private String version;
	

}
