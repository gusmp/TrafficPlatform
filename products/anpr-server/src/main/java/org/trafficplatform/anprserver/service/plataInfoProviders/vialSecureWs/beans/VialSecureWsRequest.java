package org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarmenWsRequest {

	private String usuario;
	private String password;
	private String invocante;
	private List<CarmenWsRequestImage> imagenes = new ArrayList<CarmenWsRequestImage>();
	
}
