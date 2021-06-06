package org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarmenWsResponseRoot {

	private boolean success;
	private String errorCode;
	private String exceptionClassName;
	private String exceptionMessage;
	private String exceptionTrace;
	
	private CarmenWsResponseAtrribute response;
	
	
}
