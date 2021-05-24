package org.trafficplatform.videoserver.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.thread.CaptureVideoThread;


public class VideoSourcePool {
	
	private Map<String,VideoSource> videoSourceMap = new HashMap<>();
	
	public VideoSourceEntity getVideoSourceEntity(String sourceName) throws RuntimeException {
		
		VideoSource vs = videoSourceMap.get(sourceName);
		if (vs == null) {
			throw new RuntimeException("Video source " + sourceName + " does not exists");
		}
		
		return vs.getVideoSourceEntity();
	}
	
	public CaptureVideoThread getCaptureVideoThread(String sourceName) {
		
		VideoSource vs = videoSourceMap.get(sourceName);
		if (vs == null) {
			throw new RuntimeException("Video source " + sourceName + " does not exists");
		}
		
		return vs.getCaptureVideoThread();
	}
	
	
	public Set<Map.Entry<String, VideoSource>> getEnties() {
		return this.videoSourceMap.entrySet();
	}
	
	public VideoSource getVideoSource(String sourceName) {
		return this.videoSourceMap.get(sourceName);
	}
	
	public VideoSource removeVideoSource(String sourceName) {
		return this.videoSourceMap.remove(sourceName);
	}
	
	public void addVideoSource(String sourceName, VideoSource videoSource) {
		
		this.videoSourceMap.put(sourceName, videoSource);
		
	}

}
