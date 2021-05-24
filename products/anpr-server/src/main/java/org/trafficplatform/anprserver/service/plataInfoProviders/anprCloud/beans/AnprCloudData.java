package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AnprCloudData {
	
	private List<AnprCloudVehicle> vehicles;

}
