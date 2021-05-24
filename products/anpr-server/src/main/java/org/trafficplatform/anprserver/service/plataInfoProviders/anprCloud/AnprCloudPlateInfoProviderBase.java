package org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.trafficplatform.anprserver.bean.api.response.PlateInfo;
import org.trafficplatform.anprserver.service.plataInfoProviders.IPlateInfoProvider;
import org.trafficplatform.anprserver.service.plataInfoProviders.PlateInfoProvider;
import org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans.AnprCloudRoot;
import org.trafficplatform.anprserver.service.plataInfoProviders.anprCloud.beans.AnprCloudVehicle;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AnprCloudPlateInfoProviderBase implements IPlateInfoProvider {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;
		
	@Override
	public List<PlateInfo> getPlateInfo(Resource imageResource) {
		
		List<PlateInfo> plateInfoList = new ArrayList<PlateInfo>();
		
		if ((getApikey() == null) || (getEndPoint() == null)) 
			throw new RuntimeException("The " + getPlateInfoProvider().name() + " plate info provider was not enabled / properly configured");
		
		try {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("x-api-key", getApikey());
			
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
			
			map.add("service", getAnprCloudMode());
			
			map.add("maxreads", getMaxReads());
			map.add("image", imageResource);
	
			/**/
			HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
			ResponseEntity<String> response = restTemplate.postForEntity(getEndPoint(), request, String.class);
			String responseString = response.getBody();
			/**/
			
			//String responseString = Files.readString(new File("d:\\soft\\dev\\workspace_traficong\\trafic-platform\\products\\anpr-server\\docs\\anpr\\anprcloud-mmr-response.txt").toPath());
			 
			
			log.debug("Anpr Cloud response: " + responseString);
			
			AnprCloudRoot responseBean = objectMapper.readValue(responseString, AnprCloudRoot.class);
			
			for(AnprCloudVehicle v : responseBean.getData().getVehicles()) {
				
				PlateInfo plateInfo = new PlateInfo();
				
				plateInfo.setPlateNumber(v.getPlate().getUnicodeText());
				plateInfo.setProbability(v.getPlate().getConfidence());
				
				plateInfo.setTopLeftX(v.getPlate().getPlateROI().getTopLeft().getX());
				plateInfo.setTopLeftY(v.getPlate().getPlateROI().getTopLeft().getY());
				
				plateInfo.setTopRightX(v.getPlate().getPlateROI().getTopRight().getX());
				plateInfo.setTopRightY(v.getPlate().getPlateROI().getTopRight().getY());
				
				plateInfo.setBottomRightX(v.getPlate().getPlateROI().getBottomRight().getX());
				plateInfo.setBottomRightY(v.getPlate().getPlateROI().getBottomRight().getY());

				plateInfo.setBottomLeftX(v.getPlate().getPlateROI().getBottomLeft().getX());
				plateInfo.setBottomLeftY(v.getPlate().getPlateROI().getBottomLeft().getY());
				
				if (v.getMmr() != null) {
					plateInfo.setCarMaker(v.getMmr().getMake());
					plateInfo.setModel(v.getMmr().getModel());
					plateInfo.setCategory(v.getMmr().getCategory());
					plateInfo.setCarProbability(v.getMmr().getCategoryConfidence());
				}
				
				
				plateInfoList.add(plateInfo);
			}
			
		} catch(Exception exc) {
			
			log.debug("Error getting plate info: " + exc.toString());
			log.debug("", exc);
			
		}
		
		return plateInfoList;
	}
	
	public abstract String getEndPoint();
	public abstract String getApikey();
	public abstract int getMaxReads();
	
	public abstract String getAnprCloudMode(); 
	public abstract PlateInfoProvider getPlateInfoProvider();
}
