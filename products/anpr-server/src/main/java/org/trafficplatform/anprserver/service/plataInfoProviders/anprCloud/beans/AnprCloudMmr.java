package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnprCloudMmr {

	private String category;
	private double categoryConfidence;
	private AnprCloudColor color;
	private double colorConfidence;
	private String make;
    private double makeAndModelConfidence;
    private String model;
    private double proctime;
    private String subModel;
}
