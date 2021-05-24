package org.trafficplatform.opencvlib.boot;

/*
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
*/
import org.opencv.core.Core;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenCvSetup {
	
	public static boolean setup(boolean nativeOpenCvLibLoaded) {
		
		String osName = System.getProperty("os.name").toUpperCase();
		log.debug("OS: " + osName);
		
		if (nativeOpenCvLibLoaded == false) {
			
			/*
			log.debug("Current java.library.path: " + System.getProperty("java.library.path"));
			
			if (osName.contains("LINUX") == true) {
			
				String libPath = System.getProperty("java.library.path");
				String newPath;
				String openCvNativeLibPath = "/home/gus/opencv_build/opencv/build/lib/";
	
				if (libPath == null || libPath.isEmpty()) {
				    newPath = openCvNativeLibPath;
				} else {
				    newPath = openCvNativeLibPath + File.pathSeparator + libPath;
				}
	
				System.setProperty("java.library.path", newPath);
				log.debug("java.library.path: " + System.getProperty("java.library.path"));
				
				try {
					Field field = ClassLoader.class.getDeclaredField("sys_paths");
					field.setAccessible(true);
					    
					// Create override for sys_paths
					ClassLoader classLoader = ClassLoader.getSystemClassLoader(); 
					List<String> newSysPaths = new ArrayList<String>();
					newSysPaths.add(openCvNativeLibPath);  
					newSysPaths.addAll(Arrays.asList((String[])field.get(classLoader)));
					           
					field.set(classLoader, newSysPaths.toArray(new String[newSysPaths.size()]));
				
				} catch(Exception exc) {
					log.error("Error changing lib path: " + exc.toString());
					log.error("" + exc);
					throw new RuntimeException(exc);
				}
			
			}
			*/
			
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			if (osName.contains("WINDOWS") == true) {
				// -Djava.library.path=d:\soft\dev\workspace_traficong\trafic-platform\docs\opencv\win\
				System.loadLibrary("opencv_videoio_ffmpeg452_64");
				log.info("Loaded OpenCv native library");
			}
		}
		
		return true;
	}
}
