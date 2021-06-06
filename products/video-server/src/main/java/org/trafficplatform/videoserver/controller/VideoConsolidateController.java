package org.trafficplatform.videoserver.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.trafficplatform.videoserver.bean.DateVideoCaptureFormat;
import org.trafficplatform.videoserver.bean.api.response.ConsolidateVideoResponse;
import org.trafficplatform.videoserver.bean.api.response.GenericResponse;
import org.trafficplatform.videoserver.service.video.consolidate.VideoConsolidateService;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("consolidate")
@Slf4j
public class VideoConsolidateController extends VideoBaseController {
	
	@Autowired
	private VideoConsolidateService videoConsolidateService; 
		
	@GetMapping({"/getFrames/{sourceName}/{start}/{end}", "/getFrames/{sourceName}/{start}/{end}/{fps}", "/getFrames/{sourceName}/{start}/{end}/{fps}/{width}/{height}"  })
	public ResponseEntity<StreamingResponseBody> getVideo(@PathVariable("sourceName") String sourceName, 
			@PathVariable("start") String start, 
			@PathVariable("end") String end,
			@PathVariable(name = "fps", required = false) Double fps,
			@PathVariable(name = "width", required = false) Integer width,
			@PathVariable(name = "height", required = false) Integer height) {

		log.debug(LogUtils.formatSourceName(sourceName) + "Get consolidated video " + sourceName + " from " + start + " until " + end);
		log.debug(LogUtils.formatSourceName(sourceName) + "FPS: " + fps);
		log.debug(LogUtils.formatSourceName(sourceName) + "Width: " + width);
		log.debug(LogUtils.formatSourceName(sourceName) + "Height: " + height);
		
		List<File> imageVideoList = this.videoConsolidateService.getVideoFromFrames(sourceName, 
				new DateVideoCaptureFormat(start), 
				new DateVideoCaptureFormat(end),
				videoSourcePool.getVideoSourceEntity(sourceName).getRootConsolidatedPath());
		
		return ResponseEntity.ok()
				.header("Content-Type", "multipart/x-mixed-replace; boundary="+BOUNDARY)
		        .body( (os) -> { getVideoResponse(imageVideoList, sourceName, fps, width, height, os); });
		
	}
		
	@GetMapping({"/getVideo/{sourceName}/{start}/{end}", "/getVideo/{sourceName}/{start}/{end}/{fps}", "/getVideo/{sourceName}/{start}/{end}/{fps}/{width}/{height}"  })
	public ResponseEntity<byte[]> getVideoFromVideo	(@PathVariable("sourceName") String sourceName, 
			@PathVariable("start") String start, 
			@PathVariable("end") String end,
			@PathVariable(name = "fps", required = false) Double fps,
			@PathVariable(name = "width", required = false) Integer width,
			@PathVariable(name = "height", required = false) Integer height) {
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Get video from video " + sourceName +  "  from " + start + " until " + end);
		log.debug(LogUtils.formatSourceName(sourceName) + "FPS: " + fps);
		log.debug(LogUtils.formatSourceName(sourceName) + "Width: " + width);
		log.debug(LogUtils.formatSourceName(sourceName) + "Height: " + height);
		
		File video = this.videoConsolidateService.getVideoFromVideo(sourceName, 
				new DateVideoCaptureFormat(start), 
				new DateVideoCaptureFormat(end), 
				fps,
				width,
				height,
				videoSourcePool.getVideoSourceEntity(sourceName).getRootConsolidatedPath());
		
		
		try {
			return ResponseEntity.status(HttpStatus.OK)
		            .header("Content-Type", "video/" + "mp4")
		            .header("Content-Length", String.valueOf(video.length() - 1))
		            .body(readByteRange(video.getAbsolutePath(), 0, video.length()));
			
		} catch(IOException exc) {
			log.error("Error reading the vide file: " + exc.getMessage());
			log.error("", exc);
			return null;
		}
	}
	
	
	@GetMapping("/consolidate/{sourceName}/{start}/{end}")
	public GenericResponse consolidateVideo(@PathVariable("sourceName") String sourceName, 
			@PathVariable("start") String start, 
			@PathVariable("end") String end) {
		
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Consolidated video " + sourceName + " from " + start + " until " + end);
		
		int numberFrames = this.videoConsolidateService.consolidateVideo(sourceName,
				new DateVideoCaptureFormat(start), 
				new DateVideoCaptureFormat(end));
		
		return new GenericResponse("Consolidated Video. Number of frames: " + numberFrames);

	}
	
	@GetMapping({"/list", "/list/{sourceName}"})
	public List<ConsolidateVideoResponse> getConsolidateVideoList(@PathVariable(name = "sourceName", required = false) String sourceName) {
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Getting list of consolidated videos");
		

		return videoConsolidateService.getConsolidateVideoList(sourceName, t -> new ConsolidateVideoResponse(t));
	}
	
}
