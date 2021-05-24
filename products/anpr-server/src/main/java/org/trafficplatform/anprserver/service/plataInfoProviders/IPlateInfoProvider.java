package org.trafficplatform.anprserver.service.plataInfoProviders;

import java.util.List;

import org.springframework.core.io.Resource;
import org.trafficplatform.anprserver.bean.api.response.PlateInfo;

public interface IPlateInfoProvider {
	
	public List<PlateInfo> getPlateInfo(Resource imageResource);

}
