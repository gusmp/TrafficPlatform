package org.trafficplatform.mosaicserver.bean.api.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MosaicRequest {
	
	String name;
	boolean displayName;
	List<InputSourceRequest> inputSourceList;

}
