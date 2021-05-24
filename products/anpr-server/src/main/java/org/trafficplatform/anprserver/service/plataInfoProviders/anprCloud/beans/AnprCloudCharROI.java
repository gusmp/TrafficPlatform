package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnprCloudCharROI {

	private AnprCloudPosition bottomLeft;
	private AnprCloudPosition bottomRight;
	private AnprCloudPosition topLeft;
	private AnprCloudPosition topRight;
	
}
