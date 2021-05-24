package org.trafficplatform.videoserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.repository.VideoSourceRepository;
import org.trafficplatform.videoserver.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoSourceService {

	@Autowired
	private VideoSourceRepository videoSourceRepository;
	

	public List<VideoSourceEntity> findByEnableVideoSource(Boolean enabled) {
		
		return this.videoSourceRepository.findByEnableVideoSource(enabled);
	}
	
	public List<VideoSourceEntity> findAll() {
		
		return this.videoSourceRepository.findAll();
	}
	
	public <T> List<T> findAll(Converter<VideoSourceEntity, T> converter) {
		
		List<VideoSourceEntity> videoSourceList = this.videoSourceRepository.findAll();
		
		List<T> videoSourceResponseList = new ArrayList<T>();
		for(VideoSourceEntity vse : videoSourceList) {
			
			videoSourceResponseList.add(converter.transform(vse));
		}
		
		return videoSourceResponseList;
	}

	public VideoSourceEntity findByName(String sourceName) {
		return this.videoSourceRepository.findByName(sourceName);
	}
	
	public <T> T findByName(String sourceName, Converter<VideoSourceEntity, T> converter) {
		
		VideoSourceEntity vse = this.videoSourceRepository.findByName(sourceName);
		if (vse == null) throw new RuntimeException("Video source " + sourceName + " does not exist");
		return converter.transform(vse);
	}
	
	public VideoSourceEntity update(VideoSourceEntity videoSource) {
		
		VideoSourceEntity vsbbdd = this.videoSourceRepository.findByName(videoSource.getName());
		if (vsbbdd.getId() == null) {
			throw new RuntimeException(videoSource.getName() + " cannot be updated because this source video does not exists");
		}
		
		vsbbdd.setEnableSaveVideo(videoSource.isEnableSaveVideo());
		vsbbdd.setEnableVideoSource(videoSource.isEnableVideoSource());
		vsbbdd.setNumberOfDaysToPreserve(videoSource.getNumberOfDaysToPreserve());
		vsbbdd.setRemoveOldData(videoSource.isRemoveOldData());
		
		if (vsbbdd.getRootCapturePath().equalsIgnoreCase(videoSource.getRootCapturePath()) == false) {
			
			try {
				FileUtils.moveFolder(vsbbdd.getRootCapturePath(), videoSource.getRootCapturePath());
				
			} catch(Exception exc) {
				log.error("Error updating root capture path of " + videoSource.getName() + ". Error moving data from " + vsbbdd.getRootCapturePath() + " to " + videoSource.getRootCapturePath());
				log.error("", exc);
				throw new RuntimeException("Error updating root capture path of " + videoSource.getName() + "Error moving data from " + vsbbdd.getRootCapturePath() + " to " + videoSource.getRootCapturePath());
			}
			vsbbdd.setRootCapturePath(videoSource.getRootCapturePath());
		}
		
		if (vsbbdd.getRootConsolidatedPath().equalsIgnoreCase(videoSource.getRootConsolidatedPath()) == false) {
		
			try {
				FileUtils.moveFolder(vsbbdd.getRootConsolidatedPath(), videoSource.getRootConsolidatedPath());
				
			} catch(Exception exc) {
				log.error("Error updating root consolidate path of " + videoSource.getName() + "Error moving data from " + vsbbdd.getRootCapturePath() + " to " + videoSource.getRootCapturePath());
				log.error("", exc);
				throw new RuntimeException("Error updating root consolidate path of " + videoSource.getName() + "Error moving data from " + vsbbdd.getRootCapturePath() + " to " + videoSource.getRootCapturePath());
			}
			vsbbdd.setRootConsolidatedPath(videoSource.getRootConsolidatedPath());
		}
		
		vsbbdd.setUrl(videoSource.getUrl());
		
		return this.videoSourceRepository.save(vsbbdd);
	}
	
	public VideoSourceEntity save(VideoSourceEntity newVideoSource) {
		
		if (this.videoSourceRepository.findByName(newVideoSource.getName()) != null) {
			throw new RuntimeException(newVideoSource.getName() +  " already exist");
		}
		
		newVideoSource.setEnableVideoSource(false);
		return this.videoSourceRepository.save(newVideoSource);
	}
	
	public void delete(String sourceName) {
		
		VideoSourceEntity vsbbdd = this.videoSourceRepository.findByName(sourceName);
		if (vsbbdd == null) {
			throw new RuntimeException("The video source " + sourceName + " does not exist");
		}
		
		FileUtils.deleteFolder(vsbbdd.getRootCapturePath());
		FileUtils.deleteFolder(vsbbdd.getRootConsolidatedPath());
		
		this.videoSourceRepository.delete(vsbbdd);
	}
}
