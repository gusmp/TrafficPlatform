package org.trafficplatform.videoserver.service.video.capture;

import java.io.File;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Value;
import org.trafficplatform.videoserver.entity.VideoSourceEntity;
import org.trafficplatform.videoserver.service.VideoSourcePoolService;

public abstract class VideoCaptureBase implements IVideoCapture {
	
	@Value("${videoServer.errorImageFile}")
	protected File errorImage;
	
	@Value("${videoServer.errorImageWidth}")
	protected Integer errorImageWidth;
	
	@Value("${videoServer.errorImageHeight}")
	protected Integer errorImageHeight;
	
	@Value("${videoServer.errorDelayInMs}")
	protected long errorDelayInMs;
	
	protected VideoSourceEntity videoSource;
	protected VideoSourcePoolService videoSourcePoolService;
	protected AtomicBoolean running;
	
	public VideoCaptureBase(VideoSourceEntity videoSource, VideoSourcePoolService videoSourcePoolService) {
		
		this.videoSource = videoSource;
		this.videoSourcePoolService = videoSourcePoolService;
		this.running = new AtomicBoolean(true);
	}
	
		
	private File buildNewFileName(File basePath, 
			int year, int month, int day,
			int hour, int minute, int second, int milisecond) {
		
		StringBuilder finalPath = new StringBuilder();
		
		finalPath.append(year);
		finalPath.append("/");
		finalPath.append(String.format("%02d", month));
		finalPath.append("/");
		
		
		finalPath.append(String.format("%02d", day));
		finalPath.append("/");
		
		finalPath.append(String.format("%02d", hour));
		finalPath.append("/");
		
		finalPath.append(String.format("%02d", minute));
		finalPath.append("/");
		
		finalPath.append(String.format("%02d", second));
		finalPath.append("/");
		
		new File(basePath, finalPath.toString()).mkdirs();
		
		finalPath.append(String.format("%010d", milisecond));
		finalPath.append(".jpg");
		
		File newFileName = new File(basePath,finalPath.toString());
		
		return newFileName;
		
	}
	
	protected File fullPathImage(File basePath) {
		
		LocalDateTime now = LocalDateTime.now();
		
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		
		int hour = now.getHour();
		int minute = now.getMinute();
		int second = now.getSecond();
		int milisecond = now.get(ChronoField.MICRO_OF_SECOND);
		
		return buildNewFileName(basePath, year, month, day, hour, minute, second, milisecond);
	}
	
	
	protected File fullPathImage(File basePath, Calendar calendar) {
				
		calendar.setTime(new Date());
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int milisecond = calendar.get(Calendar.MILLISECOND);
		
		return buildNewFileName(basePath, year, month, day, hour, minute, second, milisecond);
		
	}

}
