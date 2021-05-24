package org.trafficplatform.imagelib.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageUtils {
	
	public enum ENGINE { OPENCV, JDK };
	
	public static byte[] resize(File image, Integer width, Integer height, ENGINE engine) {
		
		/*
		long start = System.currentTimeMillis();
		byte[] imageByte;
		
		switch (engine) {
		case OPENCV:
			imageByte = openCvResize(image,  width,  height);
		default:
			imageByte = jdkResize(image,  width,  height);
		}
		
		long end = System.currentTimeMillis();
		log.debug("Resize time (miliseconds) - byte[] format: " + ((end - start)  + " width " + engine.name()));

		return imageByte;
		*/
		
		try {
			return resize(Files.readAllBytes(image.toPath()), width, height, engine);
		} catch(IOException exc) {
			log.error("Error loading image " + image.getAbsolutePath());
			log.error("Error: " + exc.getMessage());
			log.error("", exc);
			return null;
		}
	}
	
	public static byte[] resize(byte[] image, Integer width, Integer height, ENGINE engine) {
		
		long start = System.currentTimeMillis();
		byte[] imageByte;
		
		switch (engine) {
		case OPENCV:
			imageByte = openCvResize(image,  width,  height);
		default:
			imageByte = jdkResize(image,  width,  height);
		}
		
		long end = System.currentTimeMillis();
		log.debug("Resize time (miliseconds) - byte[] format: " + ((end - start) + " width " + engine.name()));
		
		return imageByte;
	}
	
	
	public static Mat resize(File image, Integer width, Integer height) {
		
		Mat imageMat = Imgcodecs.imread(image.getAbsolutePath());
		Mat resizedImageMat = new Mat();
		Size size = new Size(width, height);
		Imgproc.resize(imageMat, resizedImageMat, size);
		return resizedImageMat;
	}
	
	/*
	private static byte[] openCvResize(File image, Integer width, Integer height) {
		
		Mat imageMat = Imgcodecs.imread(image.getAbsolutePath());
		byte newimage[] = openCvResize(imageMat, width, height);
	    return newimage;
	}
	*/
	
	private static byte[] openCvResize(byte[] image, Integer width, Integer height) {
		
		Mat imageMat = Imgcodecs.imdecode(new MatOfByte(image), Imgcodecs.IMREAD_UNCHANGED);
		byte newimage[] = openCvResize(imageMat, width, height);
		return newimage;
	    
	}
	
	private static byte[] openCvResize(Mat imageMat, Integer width, Integer height) {
		
		Mat resizedImageMat = new Mat();
		Size size = new Size(width, height);
		
		Imgproc.resize(imageMat, resizedImageMat, size);

		MatOfByte imageResizeMatOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", resizedImageMat, imageResizeMatOfByte);
	
		return imageResizeMatOfByte.toArray();
	}
	
	/*
	private static byte[] jdkResize(File image, Integer width, Integer height) {
		
		try {
			
			return jdkResize(ImageIO.read(image), width, height);
			
		} catch(IOException exc) {
			log.error("Error loading image " + image.getAbsolutePath());
			log.error("Error: " + exc.getMessage());
			log.error("", exc);
			return null;
		}
	}
	*/
	
	private static byte[] jdkResize(byte[] image, Integer width, Integer height) {
		
		try {
			
			InputStream is = new ByteArrayInputStream(image);	
			return jdkResize(ImageIO.read(is), width, height);

			
		} catch(IOException exc) {
			log.error("Error: " + exc.getMessage());
			log.error("", exc);
			return null;
		}
	}
	
	private static byte[] jdkResize(BufferedImage image, Integer width, Integer height) throws IOException {
		
		BufferedImage resizedImageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics2D = resizedImageBuff.createGraphics();
		graphics2D.drawImage(image, 0, 0, width, height, null);
		graphics2D.dispose();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(resizedImageBuff, "jpg", baos);
		
		return baos.toByteArray();
	}
}
