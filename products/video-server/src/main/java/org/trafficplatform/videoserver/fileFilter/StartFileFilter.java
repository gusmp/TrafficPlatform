package org.trafficplatform.videoserver.fileFilter;

import java.io.File;
import java.io.FilenameFilter;

public class StartFileFilter implements FilenameFilter {

	private String prefix;
	
	public StartFileFilter(String prefix) {
		
		super();
		this.prefix = prefix;
	}

	@Override
	public boolean accept(File dir, String name) {
		
		if (name.startsWith(this.prefix) == true) {
			return true;
		}
		
		return false;
	}
	
}
