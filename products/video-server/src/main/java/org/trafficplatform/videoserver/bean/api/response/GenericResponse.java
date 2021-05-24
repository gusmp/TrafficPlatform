package org.trafficplatform.videoserver.bean.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GenericResponse {
	
	private boolean success;
	private Integer errorCode;
	private String message;
	
	public GenericResponse(String message) {
		this.success = true;
		this.errorCode = 0;
		this.message = message;
	}
}
