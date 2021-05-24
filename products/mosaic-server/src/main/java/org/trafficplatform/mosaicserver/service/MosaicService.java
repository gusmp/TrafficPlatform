package org.trafficplatform.mosaicserver.service;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.trafficplatform.mosaicserver.bean.MosaicInputSource;
import org.trafficplatform.mosaicserver.bean.MosaicInputSourcePool;
import org.trafficplatform.mosaicserver.bean.api.request.InputSourceRequest;
import org.trafficplatform.mosaicserver.bean.api.request.MosaicRequest;
import org.trafficplatform.mosaicserver.entity.InputSourceEntity;
import org.trafficplatform.mosaicserver.entity.InputSourcePositionInMosaicEntity;
import org.trafficplatform.mosaicserver.entity.InputSourcePositionInMosaicKey;
import org.trafficplatform.mosaicserver.entity.MosaicEntity;
import org.trafficplatform.mosaicserver.repository.InputSourceRepository;
import org.trafficplatform.mosaicserver.repository.MosaicRepository;

import org.trafficplatform.imagelib.utils.ImageUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class MosaicService {
	
	@Value("${mosaicServer.errorImageFile}")
	private File errorImage;
	
	@Value("${mosaicServer.errorImageWidth}")
	private Integer errorImageWidth;
	
	@Value("${mosaicServer.errorImageHeight}")
	private Integer errorImageHeight;	
	
	@Autowired
	private MosaicRepository mosaicRepository;
	
	@Autowired
	private InputSourceRepository inputSourceRepository;
	
	@Autowired
	private MosaicInputSourcePool mosaicInputSourcePool;
	
	private Semaphore removeMosaicConfigurationSemaphore = new Semaphore(1);

			
	public MosaicEntity save(MosaicRequest mosaicRequest) {
		
		MosaicEntity mosaicEntity = new MosaicEntity();
		mosaicEntity.setName(mosaicRequest.getName());
		
		
		for(InputSourceRequest isr : mosaicRequest.getInputSourceList()) {
		
			InputSourceEntity ise = inputSourceRepository.findByName(isr.getName());
			if (ise == null) {
				new RuntimeException("InputSource " + isr.getName() + " does not exist");
			}
			
			InputSourcePositionInMosaicEntity ispe = new InputSourcePositionInMosaicEntity();
			
			ispe.setId(new InputSourcePositionInMosaicKey(mosaicEntity.getId(), ise.getId()));
			ispe.setInputSource(ise);
			ispe.setPosition(isr.getPosition());
			ispe.setMosaic(mosaicEntity);
			
			mosaicEntity.getInputSourceSet().add(ispe);
		}
		
		mosaicEntity = mosaicRepository.save(mosaicEntity);
		 		
		return mosaicEntity;
		
	}
	
	public void delete(String mosaicName) {
		
		MosaicEntity mosaicEntity = mosaicRepository.findByName(mosaicName);
		if (mosaicEntity == null) {
			new RuntimeException("Mosaic name " + mosaicName + " do not exist");
		}
		
		mosaicRepository.delete(mosaicEntity);
	}
	
	public byte[] generateMosaic(String mosaicName, Integer columns) {
		
		try {
			removeMosaicConfigurationSemaphore.acquire();
		} catch(InterruptedException exc) {}
		
		log.debug("Generating mosaic " + mosaicName);
		
		// open input source
		List<MosaicInputSource> mosaicInputSourceList = mosaicInputSourcePool.getPool().get(mosaicName);
		if (mosaicInputSourceList == null) {
			
			MosaicEntity mosaicEntity = mosaicRepository.findByName(mosaicName);
			if (mosaicEntity == null) {
				removeMosaicConfigurationSemaphore.release();
				throw new RuntimeException("Mosaic " + mosaicName + " does not exists");
			}
			
			List<InputSourcePositionInMosaicEntity> inputSourceSorted = mosaicEntity.getInputSourceSet().stream().sorted( 
				(e1, e2) -> e1.getPosition() < e2.getPosition()? -1:1
			).collect(Collectors.toList());
			
			
			mosaicInputSourceList = new ArrayList<MosaicInputSource>();
			log.debug("Setting up cameras...");
			for(InputSourcePositionInMosaicEntity ispm :inputSourceSorted) {
				
				mosaicInputSourceList.add(new MosaicInputSource(mosaicEntity, ispm.getInputSource(), new VideoCapture(ispm.getInputSource().getUrl())));
				
			}
			
			mosaicInputSourcePool.getPool().put(mosaicName, mosaicInputSourceList);
		}
		
		// prepare list for reading
		List<Mat> mosaicInputSourceMatList =  new ArrayList<Mat>(mosaicInputSourceList.size());
		for(int i=0; i < mosaicInputSourceList.size(); i++) {
			mosaicInputSourceMatList.add(new Mat());
		}
		
		// row of mosaic
		List<Mat> mosaicRowList = new ArrayList<Mat>();
		int totalRows = (int) Math.ceil((double) mosaicInputSourceList.size() / columns);

		for(int i=0; i < totalRows; i++) {
			mosaicRowList.add(new MatOfByte());
		}
		
		// read image
		log.debug("Reading images from cameras...");
		for(int i=0; i < mosaicInputSourceList.size(); i++) {
			
			mosaicInputSourceList.get(i).getVideoCapture().read(mosaicInputSourceMatList.get(i));
			log.debug("Image size " + i + ": " + mosaicInputSourceMatList.get(i).size().height + " " + mosaicInputSourceMatList.get(i).size().width);
			
			if ((mosaicInputSourceMatList.get(i).size().height == 0) || 
					(mosaicInputSourceMatList.get(i).size().width == 0)) {

				mosaicInputSourceMatList.set(i, ImageUtils.resize(errorImage, errorImageWidth, errorImageHeight));			
			}
		}
		
		// write text
		for(int i=0; i < mosaicInputSourceMatList.size(); i++) {
			
			if (mosaicInputSourceList.get(i).getMosaic().isDisplayName() == true) {
			
				Size s = mosaicInputSourceMatList.get(i).size();
							
				Point namePosition = new Point(20, s.height-50);
				Scalar color = new Scalar(0,255,0);
				Imgproc.putText(mosaicInputSourceMatList.get(i), 
						mosaicInputSourceList.get(i).getInputSource().getName(), 
						namePosition, Imgproc.FONT_HERSHEY_PLAIN, 4, color, 4);
			}
		}
		
		// generate row of mosaic
		int currentRow = 0;
		for(int i=0; i < mosaicInputSourceList.size(); i+=columns) {
			
			List<Mat> imageRow = new ArrayList<Mat>();
			
			if ( (i+columns) < mosaicInputSourceList.size()) {
				
				imageRow = mosaicInputSourceMatList.subList(i, i+columns);
			
			} else {
				
				List<Mat> lastRow = mosaicInputSourceMatList.subList(i, mosaicInputSourceList.size());
				
				Mat padding = new Mat(
						mosaicInputSourceMatList.get(0).size(),
						mosaicInputSourceMatList.get(0).type());
				
				int remain = i + columns -  mosaicInputSourceList.size();
				
				for(int u=0; u < remain; u++) {
					lastRow.add(padding);
				}
				
				imageRow = lastRow;
			}
			
			Core.hconcat(imageRow, mosaicRowList.get(currentRow));
			currentRow++;
		}
		
		// join partial rows
		Mat finalMosaicMat = new Mat();
		Core.vconcat(mosaicRowList, finalMosaicMat);
		/*
		for(int i=0; i < mosaicRowList.size(); i++) {
			
			Core.vconcat(mosaicRowList, finalMosaicMat);
		}
		*/
		
		// convert to byte array
		MatOfByte finalMosaicMatOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", finalMosaicMat, finalMosaicMatOfByte);
		
		byte[] finalMosaicByteArray = new byte[(int) (finalMosaicMatOfByte.total() * finalMosaicMatOfByte.channels())];
		finalMosaicMatOfByte.get(0, 0, finalMosaicByteArray);
		
		removeMosaicConfigurationSemaphore.release();
		log.debug("Generated mosaic " + mosaicName);
		
		return finalMosaicByteArray;
	}
	
	public void removeMosaicConfiguration(String mosaicName) {
		
		log.debug("Trying to remove configuration of " + mosaicName);
		
		try {
			removeMosaicConfigurationSemaphore.acquire();
		} catch(InterruptedException exc) {}

		
		log.debug("Release " + mosaicName);
		log.debug("Release VideoCapture...");
		
		List<MosaicInputSource> mosaicInputSourceList = mosaicInputSourcePool.getPool().get(mosaicName);
		for(MosaicInputSource mis : mosaicInputSourceList) {
			mis.getVideoCapture().release();
		}
		
		log.debug("Remove configuration of " + mosaicName);
		mosaicInputSourcePool.getPool().remove(mosaicName);
		
		removeMosaicConfigurationSemaphore.release();
	}
}
