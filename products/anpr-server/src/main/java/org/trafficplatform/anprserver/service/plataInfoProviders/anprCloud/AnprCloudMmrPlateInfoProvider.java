package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.trafficplatform.anprserver.service.plataInfoProviders.PlateInfoProvider;

@Service(value = PlateInfoProvider.AnprProviderNames.ANPR_CLOUD_MMR)
public class AnprCloudMmrPlateInfoProvider extends AnprCloudPlateInfoProviderBase {
	
	@Value("${anprCloud.mmr.apiKey:#{null}}")
	private String apiKey;

	@Value("${anprCloud.mmr.endPoint:#{null}}")
	private String endPoint;

	@Value("${anprCloud.mmr.maxReads:1}")
	private int maxReads;
	
	@Override
	public String getEndPoint() {
		return this.endPoint;
	}

	@Override
	public String getApikey() {
		return this.apiKey;
	}

	@Override
	public int getMaxReads() {
		return this.maxReads;
	}
	
	@Override
	public String getAnprCloudMode() {
		return "mmr";
	}

	@Override
	public PlateInfoProvider getPlateInfoProvider() {
		return PlateInfoProvider.ANPR_CLOUD_MMR;
	} 
}
