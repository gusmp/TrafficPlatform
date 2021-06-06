package org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarmenWsResponsePlateDetails {

	private String matricula;
	private int x1,y1;
	private int x2,y2;
	private int x3,y3;
	private int x4,y4;
	private double probabilidad;
}
