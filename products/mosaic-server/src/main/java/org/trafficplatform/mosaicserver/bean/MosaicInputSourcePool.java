package org.trafficplatform.mosaicserver.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MosaicInputSourcePool {
	
	private Map<String, List<MosaicInputSource>> pool = new HashMap<String, List<MosaicInputSource>>();

}
