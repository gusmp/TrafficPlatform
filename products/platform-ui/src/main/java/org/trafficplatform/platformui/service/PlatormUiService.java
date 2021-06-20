package org.trafficplatform.platformui.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trafficplatform.platformui.entity.PlatormUiEntity;
import org.trafficplatform.platformui.repository.PlatormUiRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PlatormUiService {
	
	@Autowired
	private PlatormUiRepository platormUiRepository;
	
	public PlatormUiEntity test() {
	 	
		String pathWeights = "d:\\soft\\dev\\GitHub_gusmp\\TrafficPlatform\\products\\platform-ui\\data\\plate\\yolov4tiny\\yolov4-tiny-3l_1clase_last_start_04.weights";
		String pathCfg = "d:\\soft\\dev\\GitHub_gusmp\\TrafficPlatform\\products\\platform-ui\\data\\plate\\yolov4tiny\\yolov4-tiny-3l_1clase.cfg";
		
		//Net net = org.opencv.dnn.Dnn.readNet(pathWeights, pathCfg);
		Net net = org.opencv.dnn.Dnn.readNetFromDarknet(pathCfg, pathWeights);
		
		List<String> layerNames = net.getLayerNames();
		
		MatOfInt unconnectedOutLayers = net.getUnconnectedOutLayers();
		log.debug("Height: " + unconnectedOutLayers.size().height); // 3
		log.debug("Width: " + unconnectedOutLayers.size().width); // 1
		
		/*
		for(int i=0; i < net.getUnconnectedOutLayers(); i++) {
			
		}
		*/
		
		net.setPreferableBackend(Dnn.DNN_BACKEND_CUDA);
		net.setPreferableTarget(Dnn.DNN_TARGET_CUDA);
		
		String img = "d:\\soft\\dev\\GitHub_gusmp\\TrafficPlatform\\products\\platform-ui\\data\\img_test\\image-test.jpg";
		
		Mat imgMat = Imgcodecs.imread(img);
		Size size = new Size(320, 320);
		Scalar mean = new Scalar(0);
		boolean swapRB = true;
		boolean crop = false;
		
		Mat imgMatBlob = org.opencv.dnn.Dnn.blobFromImage(imgMat, 0.00392, size, mean, swapRB, crop);

		List<Mat> result = new ArrayList<Mat>();
		
		/*
		try {
			List<String> outBlobNames = new ArrayList<String>();
			FileReader fr = new FileReader("d:\\soft\\dev\\GitHub_gusmp\\TrafficPlatform\\products\\platform-ui\\data\\plate\\yolov4tiny\\coco.names");
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			while( (line = br.readLine()) != null) {
				outBlobNames.add(line);
			}
		} catch(Exception exc) {
			log.debug(exc.toString(), exc);
		}
		*/
		
		/*
		List<String> outBlobNames = new ArrayList<String>();
		outBlobNames.add("matricula");
		*/
		
		List<String> names = new ArrayList<>();
		List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
		List<String> layersNames = net.getLayerNames();
		outLayers.forEach((item) -> names.add(layersNames.get(item - 1)));
		List<String> outBlobNames = names;

		// https://github.com/suddh123/YOLO-object-detection-in-java/blob/code/yolo.java
		// https://github.com/suddh123/YOLO-object-detection-in-java/blob/code/yolo.java
		try {
			net.setInput(imgMatBlob);
			net.forward(result,outBlobNames);
			log.debug("Resultados: " + result.size());
			
			for (int i = 0; i < result.size(); i++) {
				
				Mat level = result.get(i);
				
				 for (int j = 0; j < level.rows(); ++j)
		         {
					 Mat row = level.row(j);
					 
					 
					 int centerX = (int)(row.get(0,0)[0] * imgMat.cols());
					 int centerY = (int)(row.get(0,1)[0] * imgMat.rows());
	                 int width   = (int)(row.get(0,2)[0] * imgMat.cols());
	                 int height  = (int)(row.get(0,3)[0] * imgMat.rows());
	                 int left    = centerX - width  / 2;
	                 int top     = centerY - height / 2;
		         }
				
				
			}
			
			
		} catch(Exception exc) {
			log.debug(exc.toString(), exc);
		}
		
		log.debug("PlatormUiRepository test");
		platormUiRepository.count();
		return null;
	}
}
