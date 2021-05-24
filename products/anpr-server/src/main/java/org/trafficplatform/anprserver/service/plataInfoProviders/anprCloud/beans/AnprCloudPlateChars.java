package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnprCloudPlateChars {
	
	private AnprCloudColor bgColor;
	private AnprCloudCharROI charROI;
	private String code;
	private AnprCloudColor color;
	private int confidence;
}
