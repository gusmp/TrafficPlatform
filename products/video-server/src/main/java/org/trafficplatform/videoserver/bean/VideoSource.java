package org.trafficplatform.videoserver.bean;

import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.thread.CaptureVideoThread;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoSource {
	
	
	private CaptureVideoThread captureVideoThread;
	private VideoSourceEntity videoSourceEntity;
		
}
