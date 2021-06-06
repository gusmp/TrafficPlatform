package org.trafficplatform.anprserver.service.plataInfoProviders.vialSecureWs.beans;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VialSecureWsRequest {

	private String usuario;
	private String password;
	private String invocante;
	private List<VialSecureWsRequestImage> imagenes = new ArrayList<VialSecureWsRequestImage>();
	
}
