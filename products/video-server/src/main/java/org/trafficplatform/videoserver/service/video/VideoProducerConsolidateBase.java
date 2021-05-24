package org.trafficplatform.videoserver.service.video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.trafficplatform.videoserver.bean.DateVideoCaptureFormat;
import org.trafficplatform.videoserver.bean.VideoSourcePool;
import org.trafficplatform.videoserver.service.PathService;
import org.trafficplatform.imagelib.utils.ImageUtils;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class VideoProducerConsolidateBase {
	
	@Autowired
	protected VideoSourcePool videoCapturePool;
	
	@Autowired
	protected PathService pathService;
	
	public static final String VIDEO_FILES_PREFIX = "vs_vid_";
	public static final String VIDEO_FILES_BASE_PATH = System.getProperty("java.io.tmpdir");
	
	public List<File> getVideoFromFrames(String sourceName, DateVideoCaptureFormat start, DateVideoCaptureFormat end, String pathData) {
		
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Get video from frames");
		log.debug(LogUtils.formatSourceName(sourceName) + "Source name: " + sourceName);
		log.debug(LogUtils.formatSourceName(sourceName) + "Start: " + start);
		log.debug(LogUtils.formatSourceName(sourceName) + "End: " + end);
		
		File rootPathSourceName = new File(pathData);
		List<File> fileList = new ArrayList<File>();
		this.pathService.getFileList(rootPathSourceName.getAbsolutePath(), rootPathSourceName, start, end, fileList);
		
		return fileList;

	}
	
	public File getVideoFromVideo(String sourceName, DateVideoCaptureFormat start, DateVideoCaptureFormat end, Double fps, Integer width, Integer height, String pathData) {
		
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Get video from images");
		log.debug(LogUtils.formatSourceName(sourceName) + "Source name: " + sourceName);
		log.debug(LogUtils.formatSourceName(sourceName) + "Start: " + start);
		log.debug(LogUtils.formatSourceName(sourceName) + "End: " + end);
		log.debug(LogUtils.formatSourceName(sourceName) + "FPS: " + fps);
		log.debug(LogUtils.formatSourceName(sourceName) + "Width: " + width);
		log.debug(LogUtils.formatSourceName(sourceName) + "Height: " + height);
		
		File rootPathSourceName = new File(pathData);
		List<File> fileList = new ArrayList<File>();
		this.pathService.getFileList(rootPathSourceName.getAbsolutePath(), rootPathSourceName, start, end, fileList);

		File videoFile = new File(VIDEO_FILES_BASE_PATH, VIDEO_FILES_PREFIX + Math.abs(new Random().nextInt()) + ".mp4");
		
		if (fps == null) {
			fps = 2.0;
		}
		
		Size frameSize = null;
		if ((width != null) && (height != null)) {
			frameSize = new Size(width, height);
		} else {
			
			if (fileList.size() > 0) {
				Mat firstImage = Imgcodecs.imread(fileList.get(0).getAbsolutePath());
				frameSize = firstImage.size();
			}
		}
		
		
		boolean isRGB = true;
		
		if (fileList.size() > 0) {
			
			VideoWriter videoWriter = new VideoWriter(videoFile.getAbsolutePath(), VideoWriter.fourcc('x', '2','6','4'), fps, frameSize, isRGB);
		
			if (videoWriter.isOpened() == true) {
				
				for(File imageFile : fileList) {
					
					Mat imageMat = null;
					if ((width != null) && (height != null)) {
						imageMat = ImageUtils.resize(imageFile, width, height);
						
					} else {
						imageMat = Imgcodecs.imread(imageFile.getAbsolutePath());
					}
					
					videoWriter.write(imageMat);
				}
				
				videoWriter.release();
				
			} else {
				log.debug("Video file cannot be opened. File: " + videoFile.getAbsolutePath());
			}
		}
		
		return videoFile;

	}
}
