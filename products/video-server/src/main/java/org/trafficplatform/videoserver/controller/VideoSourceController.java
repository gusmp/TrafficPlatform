package org.trafficplatform.videoserver.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trafficplatform.videoserver.bean.VideoSourcePool;
import org.trafficplatform.videoserver.bean.api.response.GenericResponse;
import org.trafficplatform.videoserver.bean.api.response.VideoSourceResponse;
import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.service.VideoSourcePoolService;
import org.trafficplatform.videoserver.service.VideoSourceService;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("vs")
@Slf4j
public class VideoSourceController extends BaseController {
	
	
	@Autowired
	private VideoSourcePoolService videoSourcePoolService;
	
	@Autowired
	private VideoSourceService videoSourceService;
	
	@Autowired
	private VideoSourcePool videoSourcePool;
	
	@GetMapping("/enableVideoSource/{sourceName}/{enableSaveVideo}")
	public GenericResponse enableVideo(@PathVariable("sourceName") String sourceName, 
			@PathVariable("enableSaveVideo") boolean enableSaveVideo) {
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Changing video source to " + enableSaveVideo);
	
		videoSourcePoolService.enableVideoSource(sourceName, enableSaveVideo, videoSourcePool);
		
		return new GenericResponse("Video source " + sourceName + " changed to " + enableSaveVideo);
	}
	

	@GetMapping({"/list", "/list/{sourceName}"})
	public List<VideoSourceResponse> get(@PathVariable(name = "sourceName", required = false) String sourceName) {
		
		if (sourceName == null) {
			log.debug("Get all source video details");
			
			return videoSourceService.findAll(  t -> new VideoSourceResponse(t));
			
		} else {
			log.debug(LogUtils.formatSourceName(sourceName) + "Get details of source video " + sourceName);
			
			List<VideoSourceResponse> list = new ArrayList<VideoSourceResponse>();
			list.add(videoSourceService.findByName(sourceName, t -> new VideoSourceResponse(t))); 
			return list;
		}
	}
	
	@PostMapping({"/save"})
	public GenericResponse add(@Valid @RequestBody VideoSourceEntity newVideoSource) {
		
		videoSourceService.save(newVideoSource);
		return new GenericResponse("Video source " + newVideoSource.getName() + " was added");
		
	}
	
	@PostMapping({"/update"})
	public GenericResponse update(@Valid @RequestBody VideoSourceEntity newVideoSource) {
		
		if (videoSourcePool.getVideoSource(newVideoSource.getName()) != null) 
			throw new RuntimeException("Video source " + newVideoSource.getName() + " is running. Stop it before modifying");
		
		videoSourceService.update(newVideoSource);
		return new GenericResponse("Video source " + newVideoSource.getName() + " was updated");
	}
	
	@PostMapping({"/delete/{sourceName}"})
	public GenericResponse delete(@PathVariable(name = "sourceName") String sourceName) {
		
		if (videoSourcePool.getVideoSource(sourceName) != null) 
			throw new RuntimeException("Video source " + sourceName + " is running. Stop it before deleting");
		
		videoSourceService.delete(sourceName);
		
		return new GenericResponse("Source video " + sourceName + " was deleted");
	}
}
