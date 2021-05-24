package org.trafficplatform.videoserver.service.video.produce;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;
import org.trafficplatform.videoserver.bean.DateVideoCaptureFormat;
import org.trafficplatform.videoserver.service.video.VideoProducerConsolidateBase;
import org.trafficplatform.videoserver.utils.LogUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoProducerService extends VideoProducerConsolidateBase {
	
	public int deleteVideo(String sourceName, DateVideoCaptureFormat start, DateVideoCaptureFormat end, String pathData) { 
		
		log.debug(LogUtils.formatSourceName(sourceName) + "Delete video");
		log.debug(LogUtils.formatSourceName(sourceName) + "Source name: " + sourceName);
		log.debug(LogUtils.formatSourceName(sourceName) + "Start: " + start);
		log.debug(LogUtils.formatSourceName(sourceName) + "End: " + end);
		
		List<File> fileList = getVideoFromFrames(sourceName, start, end, pathData);
		File parent;
		
		for(File f: fileList) {
			
			f.delete();
			parent = f.getParentFile();
			
			while (parent.getName().equalsIgnoreCase(sourceName) == false) {
				
				if (parent.list().length == 0) {
					parent.delete();
					parent = parent.getParentFile();
					continue;
				}
				
				break;
			}
		}
		
		return fileList.size();
		
	}
}
