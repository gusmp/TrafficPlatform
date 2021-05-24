package org.trafficplatform.videoserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trafficplatform.videoserver.bean.VideoSource;
import org.trafficplatform.videoserver.bean.VideoSourcePool;
import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.service.video.capture.IVideoCapture;
import org.trafficplatform.videoserver.service.video.capture.VideoCaptureOpenCvImagesService;
import org.trafficplatform.videoserver.service.video.capture.VideoCaptureType;
import org.trafficplatform.videoserver.thread.CaptureVideoThread;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class VideoSourcePoolService {
	
	@Autowired 
	private AutowireCapableBeanFactory beanFactory;
		
	@Autowired
	private VideoSourceService videoSourceService;
	
	public VideoSourcePool reload() {
		
		List<VideoSourceEntity> videoSourceList = videoSourceService.findByEnableVideoSource(true);
		
		log.debug("Number of video sources: " + videoSourceList.size());
		
		VideoSourcePool videoSourcePool = new VideoSourcePool();
		
		for(VideoSourceEntity vse : videoSourceList) {
			
			createVideoSource(videoSourcePool, vse);
		}
		
		return videoSourcePool;
		
	}
	
	private VideoSourcePool createVideoSource(VideoSourcePool videoSourcePool, VideoSourceEntity videoSource) {
		

		log.debug(LogUtils.formatSourceName(videoSource.getName()) + "Setting up video source " + videoSource.getName());
		
		VideoSource vs = new VideoSource();
		vs.setVideoSourceEntity(videoSource);
		
		
		IVideoCapture videoCapture = null;
		if (videoSource.getVideoCaptureType() == VideoCaptureType.OPENCV) {
			
			log.debug(LogUtils.formatSourceName(videoSource.getName()) + "Capture type " + VideoCaptureType.OPENCV.name());
			videoCapture = new VideoCaptureOpenCvImagesService(videoSource, this);	
			beanFactory.autowireBean(videoCapture);
		}
		
		CaptureVideoThread captureVideoThread = new CaptureVideoThread(videoCapture);
		vs.setCaptureVideoThread(captureVideoThread);
		log.debug(LogUtils.formatSourceName(videoSource.getName()) + "Added thread");
		
		captureVideoThread.start();
		log.debug(LogUtils.formatSourceName(videoSource.getName()) + "Thread started");
		
		videoSourcePool.addVideoSource(videoSource.getName(), vs);
		log.debug(LogUtils.formatSourceName(videoSource.getName()) + "Video source added into the video source pool");
		
		return videoSourcePool;
	}
	
	public VideoSourceEntity updateVideoSource(VideoSourceEntity videoSource) {
		
		return videoSourceService.update(videoSource);
	}
	
	public void enableVideoSource(String sourceName, boolean enableVideoSource, VideoSourcePool videoSourcePool) {
		
		if (enableVideoSource == true) {
			
			VideoSource vs = videoSourcePool.getVideoSource(sourceName);
			if (vs == null) {
				
				VideoSourceEntity vse = videoSourceService.findByName(sourceName);
				if (vse == null) throw new RuntimeException("Source name " + sourceName + " does not exists");
				
				this.createVideoSource(videoSourcePool, vse);
				
				vse.setEnableVideoSource(true);
				videoSourceService.update(vse);
				
			
			} else {
				throw new RuntimeException("Source name " + sourceName + " is already enabled");
			}
		} else {
			
			VideoSource vs = videoSourcePool.getVideoSource(sourceName);
			if (vs == null) {
				throw new RuntimeException("Source name " + sourceName + " is not enabled");
			}
			
			vs.getCaptureVideoThread().getVideoCapture().stopThread();
			
			vs.getVideoSourceEntity().setEnableVideoSource(false);
			videoSourceService.update(vs.getVideoSourceEntity());
			
			videoSourcePool.removeVideoSource(sourceName);
		}
	}
}
