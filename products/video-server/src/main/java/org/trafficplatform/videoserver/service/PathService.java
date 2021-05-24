package org.trafficplatform.videoserver.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.trafficplatform.videoserver.bean.DateVideoCaptureFormat;

@Service
public class PathService {
	
	private List<File> findFolder(File basePathSourceName, String folderCandidate) {
		
		List<File> contentList = new ArrayList<File>();
		int iFolderCandidate = Integer.valueOf(folderCandidate);
		
		File currentFolder[] = basePathSourceName.listFiles();
		Arrays.sort(currentFolder);
		
		for(File f: currentFolder) {
			
			if (Integer.valueOf(f.getName()) >= iFolderCandidate) {
				contentList.add(f);
			}
		}
		
		// caso no hay el año/mes/dia/hora/minuto/segundo disponible
		if (contentList.size() == 0) {
			if (basePathSourceName.listFiles().length > 0) {
				contentList.add(basePathSourceName.listFiles()[basePathSourceName.listFiles().length-1]);
			}
		}
		
		
		
		
		return contentList;
	}
	

	private FOLDER_LEVEL getFolderLevel(String basePathStoreImages, File file) {
		
		String currentLocation = file.getAbsolutePath().replace(basePathStoreImages, "");
		
		String pattern = Pattern.quote(File.separator);
		String currentLocationTokenized[] = currentLocation.split(pattern);
		//String currentLocationTokenized[] = currentLocation.split(File.separator);
		
		if (currentLocationTokenized.length == 1) return FOLDER_LEVEL.YEAR;
		else if (currentLocationTokenized.length == 2) return FOLDER_LEVEL.MONTH;
		else if (currentLocationTokenized.length == 3) return FOLDER_LEVEL.DAY;
		else if (currentLocationTokenized.length == 4) return FOLDER_LEVEL.HOUR;
		else if (currentLocationTokenized.length == 5) return FOLDER_LEVEL.MINUTE;
		else if (currentLocationTokenized.length == 6) return FOLDER_LEVEL.SECOND;

		return null;
		
	}
	
	
	public boolean getFileList(String basePathStoreImages, File fileSystemNode, DateVideoCaptureFormat start, DateVideoCaptureFormat end, List<File> fileList) {
		

		List<File> subFolderList;
		FOLDER_LEVEL currentFolderLevel = getFolderLevel(basePathStoreImages, fileSystemNode);

		if (currentFolderLevel == FOLDER_LEVEL.YEAR) {
			
			subFolderList = findFolder(fileSystemNode, start.getYear());
			
			if ((subFolderList.size() > 0) && (subFolderList.get(0).getName().contentEquals(start.getYear()) == false)) {
				start.setMonth("01");
				start.setDay("01");
				start.setHour("00");
				start.setMinute("00");
				start.setSecond("00");
			}
			
			for(File subFolder : subFolderList) {
				if (getFileList(basePathStoreImages, subFolder, start, end, fileList) == false) {
					return false;
				}
			}
		}
		else if (currentFolderLevel == FOLDER_LEVEL.MONTH) {
			
			subFolderList = findFolder(fileSystemNode, start.getMonth());
			
			if ((subFolderList.size() > 0) && (subFolderList.get(0).getName().contentEquals(start.getMonth()) == false)) {
				start.setDay("01");
				start.setHour("00");
				start.setMinute("00");
				start.setSecond("00");
			}
			
			
			for(File subFolder : subFolderList) {
				if (getFileList(basePathStoreImages, subFolder, start, end, fileList) == false) {
					return false;
				}
			}
		}
		else if (currentFolderLevel == FOLDER_LEVEL.DAY) {
			
			subFolderList = findFolder(fileSystemNode, start.getDay());
			
			if ((subFolderList.size() > 0) && (subFolderList.get(0).getName().contentEquals(start.getDay()) == false)) {
				start.setHour("00");
				start.setMinute("00");
				start.setSecond("00");
			}
			
			for(File subFolder : subFolderList) {
				if (getFileList(basePathStoreImages, subFolder, start, end, fileList) == false) {
					return false;
				}
			}
		}
		else if (currentFolderLevel == FOLDER_LEVEL.HOUR) {
			
			subFolderList = findFolder(fileSystemNode, start.getHour());
			
			if ((subFolderList.size() > 0) && (subFolderList.get(0).getName().contentEquals(start.getHour()) == false)) {
				start.setMinute("00");
				start.setSecond("00");
			}
			
			for(File subFolder : subFolderList) {
				if (getFileList(basePathStoreImages, subFolder, start, end, fileList) == false) {
					return false;
				}
			}
		}
		else if (currentFolderLevel == FOLDER_LEVEL.MINUTE) {
			
			subFolderList = findFolder(fileSystemNode, start.getMinute());
			
			if ((subFolderList.size() > 0) && (subFolderList.get(0).getName().contentEquals(start.getMinute()) == false)) {
				start.setSecond("00");
			}
			
			for(File subFolder : subFolderList) {
				if (getFileList(basePathStoreImages, subFolder, start, end, fileList) == false) {
					return false;
				}
			}
		}
		else if (currentFolderLevel == FOLDER_LEVEL.SECOND) {
						
			subFolderList = findFolder(fileSystemNode, start.getSecond());
			
			start.setMonth("00");
			start.setDay("00");
			start.setHour("00");
			start.setMinute("00");
			start.setSecond("00");
			
			for(File subFolder : subFolderList) {
				
				String currentLocation = subFolder.getAbsolutePath().replace(basePathStoreImages, "");
				currentLocation = currentLocation.replace(File.separator,"");
				
				if(Long.valueOf(currentLocation).longValue() > Long.valueOf(end.getDateVideoCaptureFormat()).longValue()) {
					return false;
				}
				
				File currentFolder[] = subFolder.listFiles();
				Arrays.sort(currentFolder);
				fileList.addAll( Arrays.asList(currentFolder));
			}
		}
		
		return true;
	}
}
