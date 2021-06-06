package org.trafficplatform.anprserver.service.plataInfoProviders.vialSecureWs.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VialSecureWsResponseRoot {

	private boolean success;
	private String errorCode;
	private String exceptionClassName;
	private String exceptionMessage;
	private String exceptionTrace;
	
	private VialSecureWsResponseAtrribute response;
	
	
}
