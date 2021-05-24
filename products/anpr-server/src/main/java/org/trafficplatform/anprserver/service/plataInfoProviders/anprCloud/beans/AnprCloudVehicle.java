package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnprCloudVehicle {

	private AnprCloudMmr mmr;
	private AnprCloudPlate plate;
	
}
