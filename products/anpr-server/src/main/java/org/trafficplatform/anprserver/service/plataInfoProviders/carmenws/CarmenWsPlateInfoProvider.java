package org.trafficplatform.anprserver.service.plataInfoProviders.carmenws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trafficplatform.anprserver.bean.api.response.PlateInfo;
import org.trafficplatform.anprserver.service.plataInfoProviders.IPlateInfoProvider;
import org.trafficplatform.anprserver.service.plataInfoProviders.PlateInfoProvider;
import org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans.CarmenWsRequest;
import org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans.CarmenWsRequestImage;
import org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans.CarmenWsResponsePlateDetails;
import org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans.CarmenWsResponseRespuesta;
import org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans.CarmenWsResponseRoot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service(value = PlateInfoProvider.AnprProviderNames.CARMENWS)
@Slf4j
public class CarmenWsPlateInfoProvider implements IPlateInfoProvider {

	@Value("${carmenWs.endPoint:#{null}}")
	private String endPoint;
	
	@Value("${carmenWs.userName:#{null}}")
	private String userName;
	
	@Value("${carmenWs.password:#{null}}")
	private String password;
	
	@Value("${carmenWs.invoker:#{null}}")
	private String invoker;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public List<PlateInfo> getPlateInfo(Resource imageResource) {
		
		List<PlateInfo> plateInfoList = new ArrayList<PlateInfo>();
		
		if ((this.endPoint == null) || (this.userName == null) || (this.password == null) || (this.invoker == null)) 
			throw new RuntimeException("The " + PlateInfoProvider.AnprProviderNames.CARMENWS + "plate info provider was not enabled / properly configured");
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			CarmenWsRequest requestPayload = new CarmenWsRequest();
			requestPayload.setUsuario(this.userName);
			requestPayload.setPassword(this.password);
			requestPayload.setInvocante(this.invoker);

			byte[] imageByte = new byte[(int)imageResource.contentLength()];
			imageResource.getInputStream().read(imageByte, 0, (int)imageResource.contentLength());
			imageResource.getInputStream().close();
			
			CarmenWsRequestImage requestImage = new CarmenWsRequestImage();
			requestImage.setId( System.currentTimeMillis() + "-" + new Random().nextInt(10000) );
			requestImage.setImagen(Base64.encodeBase64String(imageByte));
	
			requestPayload.getImagenes().add(requestImage);
			
			
			String requestPayLoadString = objectMapper.writeValueAsString(requestPayload);
			log.debug("Payload: " + requestPayLoadString);
			
			HttpEntity<String> request = new HttpEntity<String>(requestPayLoadString, headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(this.endPoint, request, String.class);
			String responseString = response.getBody();
			
			// Testing
			//String responseString = "{\"success\":true,\"errorCode\":null,\"exceptionClassName\":\"\",\"exceptionMessage\":\"\",\"exceptionTrace\":\"\",\"response\":{\"respuesta\":[{\"id\":\"1621421729692-2091\",\"matriculaMetadatos\":[{\"matricula\":\"B2993PT\",\"x1\":453,\"y1\":644,\"x2\":573,\"y2\":664,\"x3\":571,\"y3\":687,\"x4\":450,\"y4\":668,\"probabilidad\":78.0},{\"matricula\":\"6260JCV\",\"x1\":66,\"y1\":408,\"x2\":145,\"y2\":412,\"x3\":145,\"y3\":426,\"x4\":66,\"y4\":422,\"probabilidad\":52.0},{\"matricula\":\"2686DDL\",\"x1\":626,\"y1\":307,\"x2\":694,\"y2\":318,\"x3\":693,\"y3\":332,\"x4\":625,\"y4\":321,\"probabilidad\":6.0},{\"matricula\":\"411IHI\",\"x1\":199,\"y1\":-4,\"x2\":315,\"y2\":13,\"x3\":314,\"y3\":58,\"x4\":197,\"y4\":41,\"probabilidad\":2.0}]}]}}";

			CarmenWsResponseRoot responseBean = objectMapper.readValue(responseString, CarmenWsResponseRoot.class);
			
			if (responseBean.isSuccess() == true) {
			
				if (responseBean.getResponse() != null) {
					
					for(CarmenWsResponseRespuesta respuesta : responseBean.getResponse().getRespuesta()) {
						
						for(CarmenWsResponsePlateDetails matriculaMetadatos : respuesta.getMatriculaMetadatos()) {
							
							PlateInfo plateInfo = new PlateInfo();
							
							plateInfo.setPlateNumber(matriculaMetadatos.getMatricula());
							plateInfo.setProbability(matriculaMetadatos.getProbabilidad());
							
							plateInfo.setTopLeftX(matriculaMetadatos.getX1());
							plateInfo.setTopLeftY(matriculaMetadatos.getY1());
							
							plateInfo.setTopRightX(matriculaMetadatos.getX2());
							plateInfo.setTopRightY(matriculaMetadatos.getY2());
							
							plateInfo.setBottomRightX(matriculaMetadatos.getX3());
							plateInfo.setBottomRightY(matriculaMetadatos.getY3());
							
							plateInfo.setBottomLeftX(matriculaMetadatos.getX4());
							plateInfo.setBottomLeftY(matriculaMetadatos.getY4());
							
							
							plateInfoList.add(plateInfo);
						}
					}
				}
			}
			
			
			log.debug("Response: " + responseString);
		
		} catch(JsonProcessingException exc) {
			
			log.debug("Error serializing request: " + exc.toString());
			log.debug("", exc);
		} catch(IOException exc) {
			
			log.debug("Error reading image : " + exc.toString());
			log.debug("", exc);
		}
		
		return plateInfoList;
	}
}
