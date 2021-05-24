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
import org.trafficplatform.videoserver.bean.api.response.GenericResponse;
import org.trafficplatform.videoserver.service.video.produce.VideoProducerService;
import org.trafficplatform.videoserver.thread.CaptureVideoThread;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("video")
@Slf4j
public class VideoProducerController extends VideoBaseController {
	
	@Autowired
	private VideoProducerService videoProducerService; 
	
	@GetMapping({"/getFrames/{sourceName}/{start}/{end}", "/getFrames/{sourceName}/{start}/{end}/{fps}", "/getFrames/{sourceName}/{start}/{end}/{fps}/{width}/{height}"  })
	public ResponseEntity<StreamingResponseBody> getVideoFromFrames(@PathVariable("sourceName") String sourceName, 
			@PathVariable("start") String start, 
			@PathVariable("end") String end,
			@PathVariable(name = "fps", required = false) Double fps,
			@PathVariable(name = "width", required = false) Integer width,
			@PathVariable(name = "height", required = false) Integer height) {
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Get video from frames " + sourceName +  "  from " + start + " until " + end);
		log.debug(LogUtils.formatSourceName(sourceName) + "FPS: " + fps);
		log.debug(LogUtils.formatSourceName(sourceName) + "Width: " + width);
		log.debug(LogUtils.formatSourceName(sourceName) + "Height: " + height);
		
		List<File> imageVideoList = this.videoProducerService.getVideoFromFrames(sourceName, 
				new DateVideoCaptureFormat(start), 
				new DateVideoCaptureFormat(end),
				this.videoSourcePool.getVideoSourceEntity(sourceName).getRootCapturePath());
		
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
		
		File video = this.videoProducerService.getVideoFromVideo(sourceName, 
				new DateVideoCaptureFormat(start), 
				new DateVideoCaptureFormat(end), 
				fps,
				width,
				height,
				this.videoSourcePool.getVideoSourceEntity(sourceName).getRootCapturePath());
		
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
	
	
	@GetMapping({"/currentVideo/{sourceName}","/currentVideo/{sourceName}/{width}/{height}"})
	public ResponseEntity<StreamingResponseBody> getCurrentVideo(@PathVariable("sourceName") String sourceName,
			@PathVariable(name = "width", required = false) Integer width,
			@PathVariable(name = "height", required = false) Integer height) {
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Get live video from " + sourceName);
		log.debug(LogUtils.formatSourceName(sourceName) + "Width: " + width);
		log.debug(LogUtils.formatSourceName(sourceName) + "Height: " + height);
		
		CaptureVideoThread captureVideoThread = videoSourcePool.getCaptureVideoThread(sourceName);
		
		return ResponseEntity.ok()
				.header("Content-Type", "multipart/x-mixed-replace; boundary="+BOUNDARY)
		        .body( (os) -> { getVideoResponse(captureVideoThread.getVideoCapture(), width, height, os); });
	} 
	
	
	@GetMapping("/currentImage/{sourceName}")
	public ResponseEntity<StreamingResponseBody> currentImage(@PathVariable("sourceName") String sourceName) {
		
		CaptureVideoThread captureVideoThread = videoSourcePool.getCaptureVideoThread(sourceName);
		
		byte img[] = captureVideoThread.getVideoCapture().getCurrentImage();
		
		return ResponseEntity.ok()
				.contentLength(img.length)
				.header("Content-Type", "image/jpeg")
				.body( (os) -> { os.write(img); });
	}
	

	@GetMapping("/enableSaveVideo/{sourceName}/{enableSaveVideo}")
	public GenericResponse enableVideo(@PathVariable("sourceName") String sourceName, @PathVariable("enableSaveVideo") boolean enableSaveVideo) {
		
		CaptureVideoThread captureVideoThread = videoSourcePool.getCaptureVideoThread(sourceName);
		captureVideoThread.getVideoCapture().setEnableSaveVideo(enableSaveVideo);
		
		return new GenericResponse("Current status: " + captureVideoThread.getVideoCapture().isEnableSaveVideo());
	}
	
	
	@GetMapping("/delete/{sourceName}/{start}/{end}")
	public GenericResponse deleteVideo(@PathVariable("sourceName") String sourceName, 
			@PathVariable("start") String start, 
			@PathVariable("end") String end) {
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Delete video " + sourceName + " from " + start + " until " + end);
		
		int totalDeleteFrames = this.videoProducerService.deleteVideo(sourceName, 
				new DateVideoCaptureFormat(start), 
				new DateVideoCaptureFormat(end),
				this.videoSourcePool.getVideoSourceEntity(sourceName).getRootCapturePath());
		
		return new GenericResponse("OK. Delete frames: " + totalDeleteFrames);
		
	}
}
