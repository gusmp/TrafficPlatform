package org.trafficplatform.videoserver.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.trafficplatform.videoserver.service.video.capture.IVideoCapture;
import org.trafficplatform.imagelib.utils.ImageUtils;
import org.trafficplatform.videoserver.utils.LogUtils;
import org.trafficplatform.imagelib.utils.ImageUtils.ENGINE;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class VideoBaseController extends BaseController {
	
	protected static final String BOUNDARY = "---------------------------974767299852498929531610573";
	
	protected void getVideoResponse(List<File> imageVideoList, String sourceName, Double fps, Integer width, Integer height, OutputStream os) throws IOException {
		
		byte imageByte[];
		int frameNumber = 0;
		
		long delayInMilisecons = 0;
		
		if (fps != null) {
			if (fps > 0) {
				delayInMilisecons = Math.round(1000/fps);
			}
		}
		
		for(File image: imageVideoList) {
			
			log.debug(LogUtils.formatSourceName(sourceName) + "Sending frame " + image.getAbsolutePath());
			
			if ((width == null) || (height == null)) {
				imageByte = FileUtils.readFileToByteArray(image);
			} else {
				imageByte = ImageUtils.resize(image, width, height, ENGINE.JDK);
			}
			
			os.write(("--"+BOUNDARY+"\n").getBytes());
			os.write(("Content-Type: image/jpeg\n").getBytes());
			os.write(("Content-length: " +  imageByte.length + "\n\n").getBytes());
			os.write(imageByte);
			os.write("\n\n".getBytes());
			
			frameNumber++;
			
			if (delayInMilisecons > 0) {
				try {
					Thread.sleep(delayInMilisecons);
				} catch(Exception exc) {}
			}
		}
		
		log.debug(LogUtils.formatSourceName(sourceName) + "End sending frame");
		log.debug(LogUtils.formatSourceName(sourceName) + "Total frames: " + frameNumber);
		
	}
	
	protected void getVideoResponse(IVideoCapture videoCapture, Integer width, Integer height, OutputStream os) throws IOException {
		
		byte imageByte[];
		
		while(true) {
			
			imageByte = videoCapture.getCurrentImage();
			
			if ((width != null) && (height != null)) {
				imageByte = ImageUtils.resize(imageByte, width, height, ENGINE.JDK);
			}
			
			os.write(("--"+BOUNDARY+"\n").getBytes());
			os.write(("Content-Type: image/jpeg\n").getBytes());
			os.write(("Content-length: " +  imageByte.length + "\n\n").getBytes());
			os.write(imageByte);
			os.write("\n\n".getBytes());
		}
	}
	
	protected byte[] readByteRange(String filename, long start, long end) throws IOException {

	    FileInputStream inputStream = new FileInputStream( filename);
	    ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream();
	    byte[] data = new byte[1024];
	    int nRead;
	    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
	        bufferedOutputStream.write(data, 0, nRead);
	    }
	    inputStream.close();
	    bufferedOutputStream.flush();
	    byte[] result = new byte[(int) (end - start)];
	    System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, (int) (end - start));
	    return result;
	}

}
