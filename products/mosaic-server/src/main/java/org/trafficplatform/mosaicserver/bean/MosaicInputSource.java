package org.trafficplatform.mosaicserver.bean;

import org.opencv.videoio.VideoCapture;
import org.trafficplatform.mosaicserver.entity.InputSourceEntity;
import org.trafficplatform.mosaicserver.entity.MosaicEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MosaicInputSource {
	
	private MosaicEntity mosaic;
	private InputSourceEntity inputSource;
	private VideoCapture videoCapture;

}
