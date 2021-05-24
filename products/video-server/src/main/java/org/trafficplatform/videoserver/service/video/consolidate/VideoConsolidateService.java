package org.trafficplatform.videoserver.service.video.consolidate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trafficplatform.videoserver.bean.DateVideoCaptureFormat;
import org.trafficplatform.videoserver.entity.ConsolidatedVideoEntity;
import org.trafficplatform.videoserver.repository.VideoConsolidateRepository;
import org.trafficplatform.videoserver.repository.VideoSourceRepository;
import org.trafficplatform.videoserver.service.Converter;
import org.trafficplatform.videoserver.service.video.VideoProducerConsolidateBase;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class VideoConsolidateService extends VideoProducerConsolidateBase {

	
	@Autowired
	private VideoConsolidateRepository videoConsolidateRepository;
	
	
	@Autowired
	private VideoSourceRepository videoSourceRepository;
	
	public int consolidateVideo(String sourceName, DateVideoCaptureFormat start, DateVideoCaptureFormat end) {
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Consolidate video");
		log.debug(LogUtils.formatSourceName(sourceName) + "Source name: " + sourceName);
		log.debug(LogUtils.formatSourceName(sourceName) + "Start: " + start);
		log.debug(LogUtils.formatSourceName(sourceName) + "End: " + end);
		

		File rootPathSourceName = new File(videoCapturePool.getVideoSourceEntity(sourceName).getRootCapturePath());		
		List<File> fileList = new ArrayList<File>();
		this.pathService.getFileList(rootPathSourceName.getAbsolutePath(), rootPathSourceName, start, end, fileList);

		File target = null;
		int numberFrame = 0;
		for(File source: fileList) {
			
			try {
				target = new File(source.getAbsolutePath().replace("\\", "/").replace(
						videoCapturePool.getVideoSourceEntity(sourceName).getRootCapturePath(), 
						videoCapturePool.getVideoSourceEntity(sourceName).getRootConsolidatedPath()));
				
				
				FileUtils.copyFile(source,  target);
				numberFrame++;
			
			} catch(IOException exc) {
				log.error(LogUtils.formatSourceName(sourceName) + "Error consolidating video");
				log.error(LogUtils.formatSourceName(sourceName) + source.getAbsolutePath() + " could not be copied to " + target.getAbsolutePath());
				log.error(LogUtils.formatSourceName(sourceName), exc);
			}
		}

		ConsolidatedVideoEntity consolidatedVideo = new ConsolidatedVideoEntity();
		consolidatedVideo.setStart(start.getDateVideoCaptureFormat());
		consolidatedVideo.setEnd(end.getDateVideoCaptureFormat());
		consolidatedVideo.setVideoSource(videoSourceRepository.findByName(sourceName));
		consolidatedVideo.setCreatedAt(new Date());
				
		videoConsolidateRepository.save(consolidatedVideo);
		
		return numberFrame;
	}
	
	public <T> List<T> getConsolidateVideoList(String sourceName, Converter<ConsolidatedVideoEntity, T> converter) {
		
		List<ConsolidatedVideoEntity> videoConsolidatedList;
		if (sourceName == null) {
			videoConsolidatedList = videoConsolidateRepository.findAll();
		} else {
			videoConsolidatedList = videoConsolidateRepository.findBySourceName(sourceName);
		}
		
		List<T> consolidatedVideoResponseList = new ArrayList<T>();
		for(ConsolidatedVideoEntity cve : videoConsolidatedList) {
		
			consolidatedVideoResponseList.add(converter.transform(cve));
		}
		
		return consolidatedVideoResponseList;
	}
	
}
