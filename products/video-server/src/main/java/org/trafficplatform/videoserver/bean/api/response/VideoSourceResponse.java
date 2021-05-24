package org.trafficplatform.videoserver.bean.api.response;

import org.trafficplatform.videoserver.entity.VideoSourceEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoSourceResponse {
	
	private Long id;
	
	private boolean enableVideoSource;
	private boolean enableSaveVideo;
	
	private String name;
	private String url;
	
	private boolean removeOldData;
	private Integer numberOfDaysToPreserve;
	private String rootCapturePath;
	
	private String rootConsolidatedPath;
	
	private String videoCaptureType;
	
	public VideoSourceResponse(VideoSourceEntity vse) {
		
		this.id = vse.getId();
		this.name = vse.getName();
		this.rootCapturePath = vse.getRootCapturePath();
		this.rootConsolidatedPath =  vse.getRootConsolidatedPath();
		this.url = vse.getUrl();
		this.videoCaptureType = vse.getVideoCaptureType().name();
		this.enableVideoSource = vse.isEnableVideoSource();
		this.enableSaveVideo = vse.isEnableSaveVideo();
		this.removeOldData = vse.isRemoveOldData();
		this.numberOfDaysToPreserve = vse.getNumberOfDaysToPreserve();
	}

}
