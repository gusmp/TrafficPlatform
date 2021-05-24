package org.trafficplatform.anprserver.bean.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlateInfo {

	private String plateNumber;
	private double probability;
	
	private int topLeftX; 
	private int topLeftY;
	
	private int topRightX;
	private int topRightY;
	
	private int bottomRightX;
	private int bottomRightY;
	
	private int bottomLeftX;
	private int bottomLeftY;
	
	private String carMaker;
	private String model;
	private String category;
	private double carProbability;
	
}
