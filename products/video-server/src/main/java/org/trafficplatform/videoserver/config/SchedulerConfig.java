package org.trafficplatform.videoserver.config;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.trafficplatform.videoserver.bean.DateVideoCaptureFormat;
import org.trafficplatform.videoserver.bean.VideoSource;
import org.trafficplatform.videoserver.bean.VideoSourcePool;
import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.fileFilter.StartFileFilter;
import org.trafficplatform.videoserver.service.video.VideoProducerConsolidateBase;
import org.trafficplatform.videoserver.service.video.produce.VideoProducerService;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@Slf4j
public class SchedulerConfig {
	
	
	@Autowired
	private VideoSourcePool videoSourcePool;
	
	@Autowired
	private VideoProducerService videoProducerService;
	
	
	@Value("${scheduler.removeOldVideos.hoursToPreserve}")
	private int hoursToPreserveOldVideos;
	
	private StartFileFilter videoFileFilter = new StartFileFilter(VideoProducerConsolidateBase.VIDEO_FILES_PREFIX);
	
	@Scheduled(cron = "${scheduler.removeOldData.cron}")
	public void deleteOldData() {
		
				
		for(Map.Entry<String, VideoSource> videoSourceEntry : videoSourcePool.getEnties()) {
			
			VideoSourceEntity videoSourceEntity = videoSourcePool.getVideoSourceEntity(videoSourceEntry.getKey());
			
			if (videoSourceEntity.isRemoveOldData() == true) {
				
				log.debug(LogUtils.formatSourceName(videoSourceEntity.getName()) + "Deleting old data of source video " + videoSourceEntity.getName());
				
				LocalDateTime endLocalDateTime = LocalDateTime.now().minusDays(videoSourceEntity.getNumberOfDaysToPreserve());
				DateVideoCaptureFormat end = new DateVideoCaptureFormat(endLocalDateTime);
				DateVideoCaptureFormat start = new DateVideoCaptureFormat(endLocalDateTime.minusYears(1));
				
				log.debug(LogUtils.formatSourceName(videoSourceEntity.getName()) + "Source: " + videoSourceEntity.getName());
				log.debug(LogUtils.formatSourceName(videoSourceEntity.getName()) + "Start: " + start.toString());
				log.debug(LogUtils.formatSourceName(videoSourceEntity.getName()) + "End: " + end.toString());
				
				videoProducerService.deleteVideo(videoSourceEntity.getName(), start, end, videoSourceEntity.getRootCapturePath());
			} 
		}
	}
	
	@Scheduled(cron = "${scheduler.removeOldVideos.cron}")
	public void deleteOldVideos() {
		
		
		String[] videoFileNameArray = new File(VideoProducerConsolidateBase.VIDEO_FILES_BASE_PATH).list(videoFileFilter);
		
		LocalDateTime endLocalDateTime = LocalDateTime.now().minusHours(hoursToPreserveOldVideos);
		log.debug("Videos before " + endLocalDateTime + " will be deleted");
		
		for(String videoFileName :videoFileNameArray) {
			
			File v = new File(VideoProducerConsolidateBase.VIDEO_FILES_BASE_PATH, videoFileName);
			log.debug("Found old video: " + v.getAbsolutePath());
			
			LocalDateTime vDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(v.lastModified()), TimeZone.getDefault().toZoneId());
			log.debug("Video date: " + vDateTime.toString());
			
			if (vDateTime.isBefore(endLocalDateTime) == true) {
				v.delete();
				log.debug("Video " + v.getAbsolutePath() + " was deleted");
			}
		}
	}
}

