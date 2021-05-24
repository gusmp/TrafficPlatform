package org.trafficplatform.videoserver.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {

	public static void moveFolder(String source, String target) throws IOException {

		File f = new File(target);
		f.getParentFile().mkdirs();
		Files.move(new File(source).toPath(), f.toPath());
	}

	public static void deleteFolder(String target) {
		deleteFolder(new File(target));
	}
	
	public static void deleteFolder(File target) {

		File[] files = target.listFiles();

		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		target.delete();
	}
}
