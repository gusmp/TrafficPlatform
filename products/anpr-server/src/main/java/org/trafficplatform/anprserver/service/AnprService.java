package org.trafficplatform.anprserver.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.trafficplatform.anprserver.bean.api.response.PlateInfo;
import org.trafficplatform.anprserver.service.plataInfoProviders.IPlateInfoProvider;
import org.trafficplatform.anprserver.service.plataInfoProviders.PlateInfoProvider;

@Service
@Transactional
public class AnprService {
	

	@Autowired
	private Map<String, IPlateInfoProvider> plateInfoProvidersMap;
	
	public List<PlateInfo> getPlateInfo(PlateInfoProvider plateInfoProvider, Resource imageResource) {
		
		IPlateInfoProvider plataInfoProvider = plateInfoProvidersMap.get(plateInfoProvider.toString());
		return plataInfoProvider.getPlateInfo(imageResource);
	}
}
