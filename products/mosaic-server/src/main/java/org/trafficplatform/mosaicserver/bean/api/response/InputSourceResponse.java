package org.trafficplatform.mosaicserver.bean.api.response;

import org.trafficplatform.mosaicserver.entity.InputSourceEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputSourceResponse {
	
	private String name;
	private String url;
	
	public InputSourceResponse(InputSourceEntity inputSourceEntity) {
		
		this.name = inputSourceEntity.getName();
		this.url = inputSourceEntity.getUrl();
	}
}
