package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnprCloudPlate {
	
	private AnprCloudColor bgColor;
	private AnprCloudColor color;
	private int confidence;
	private String country;
	
	private List<AnprCloudPlateChars> plateChars;
	private AnprCloudPlateRoi plateROI;
	
	private String plateType;
	private int proctime;
	private String state;
	private String unicodeText;
	
}
