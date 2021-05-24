package org.trafficplatform.anprserver.service.plataInfoProviders.carmenws.beans;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarmenWsResponseRespuesta {

	private String id;
	private List<CarmenWsResponsePlateDetails> matriculaMetadatos;
	
}
