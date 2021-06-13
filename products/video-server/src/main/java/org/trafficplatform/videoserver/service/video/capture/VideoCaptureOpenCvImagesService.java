package org.trafficplatform.videoserver.service.video.capture;


import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.trafficplatform.imagelib.utils.ImageUtils;
import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.service.VideoSourcePoolService;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VideoCaptureOpenCvImagesService extends VideoCaptureBase {
		

	private VideoCapture videoCapture;
	
	private final ReentrantLock currentImageReady = new ReentrantLock(true);
	private Mat currentImage;
	
	public VideoCaptureOpenCvImagesService(VideoSourceEntity videoSource, VideoSourcePoolService videoSourcePoolService) {
		
		super(videoSource, videoSourcePoolService);
		this.currentImage = null;
	}
	
	@Override
	public double getWidth() {
		return videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
	}

	@Override
	public double getHeight() {
		return videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
	}
	
	private VideoCapture openCamera(VideoCapture cap, String url) {
		
		if (this.videoSource.getName().contains("WEBCAM") == true) {
			cap = new VideoCapture( Integer.valueOf(url));
		}
		else {
			cap = new VideoCapture(url);
		}
		
		if (cap.isOpened()) log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "Camera " + url + "...OK");
		else  log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "Error opening camera " + url);
		
		return cap;
	}
	
		
	@Override
	public void startThread() {
		
		this.videoCapture = openCamera(this.videoCapture, this.videoSource.getUrl());
		
		log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "Running camera " + this.videoSource.getName() 
				+ " " + "Size: " + getWidth() + " x " + getHeight());
		
		long numberFrames = 0;
		long startTime = System.currentTimeMillis();
		boolean readError = false;
		boolean imageRead = false;
		
		this.currentImage = new Mat();	
		
		while(this.running.get()) {
			
			
			currentImageReady.lock();
			
			imageRead = this.videoCapture.read(this.currentImage);
			numberFrames++;
			readError = false;
			
			//if ((this.currentImage.size().height == 0.0) || (this.currentImage.size().width == 0.0)) {
			if (imageRead == false) {
				log.error(LogUtils.formatSourceName(this.videoSource.getName())  + "Error retreaving the image from source " + this.videoSource.getUrl());
				
				this.currentImage = ImageUtils.resize(errorImage, errorImageWidth, errorImageHeight);
				readError = true;
	
			}
			
			currentImageReady.unlock();
			
			if (readError == true) {
				try { 
					Thread.sleep(errorDelayInMs);
					this.videoCapture = openCamera(this.videoCapture, this.videoSource.getUrl());
				} catch(Exception exc) {}
			}
			
			
			if (this.videoSource.isEnableSaveVideo() == true) {
				String n = fullPathImage( new File(this.videoSource.getRootCapturePath()) ).getAbsolutePath();
				Imgcodecs.imwrite(n, this.currentImage);
			}
			
			if ((numberFrames % 100) == 0) {
				log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "FPS: " + numberFrames / ((System.currentTimeMillis() - startTime) / 1000) ); 
			}
		}
		
		log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "Thread ended");
	}
	
	@Override
	public void stopThread() {
		
		log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "Stopping thread...");
		this.running.set(false);
		log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "Stopped");
		
		
	}
	
	@Override
	public byte[] getCurrentImage() {
		
		currentImageReady.lock();
		
		MatOfByte currentImageMatOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", this.currentImage, currentImageMatOfByte);
		
		currentImageReady.unlock();
		
		byte[] currentImageByteArray = new byte[(int) (currentImageMatOfByte.total() * currentImageMatOfByte.channels())];
		currentImageMatOfByte.get(0, 0, currentImageByteArray);
		
		return currentImageByteArray;
	}

	@Override
	public boolean setEnableSaveVideo(boolean enableSaveVideo) {
		
		log.debug(LogUtils.formatSourceName(this.videoSource.getName()) + "Set enable save video to " + enableSaveVideo);
		this.videoSource.setEnableSaveVideo(enableSaveVideo);
		this.videoSourcePoolService.updateVideoSource(this.videoSource);
		return enableSaveVideo;
	}
	
	@Override
	public boolean isEnableSaveVideo() {
		return this.videoSource.isEnableSaveVideo();
	}

}
