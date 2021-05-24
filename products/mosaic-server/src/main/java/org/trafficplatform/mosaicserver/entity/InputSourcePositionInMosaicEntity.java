package org.trafficplatform.mosaicserver.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "INPUT_SOURCE_POSITION_MOSAIC")
@Getter
@Setter
public class InputSourcePositionInMosaicEntity {

	@EmbeddedId
	InputSourcePositionInMosaicKey id;
	
	@ManyToOne
    @MapsId("inputSourceId")
    @JoinColumn(name = "input_source_id")
    InputSourceEntity inputSource;

	@ManyToOne
    @MapsId("mosaicId")
    @JoinColumn(name = "mosaic_id")
    MosaicEntity mosaic;
	
	Integer position;
   	
}

