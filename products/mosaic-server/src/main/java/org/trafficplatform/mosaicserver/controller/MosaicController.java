package org.trafficplatform.mosaicserver.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.trafficplatform.mosaicserver.bean.api.request.MosaicRequest;
import org.trafficplatform.mosaicserver.bean.api.response.GenericResponse;
import org.trafficplatform.mosaicserver.service.MosaicService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("mosaic")
@Slf4j
public class MosaicController extends BaseController {
	
	@Autowired
	private MosaicService mosaicService;
	
	private AtomicBoolean stopMosaic = new AtomicBoolean();
	
	@PostMapping({"/save"})
	public GenericResponse save(@RequestBody MosaicRequest mosaicRequest) {
		
		log.debug("Add new mosaic " + mosaicRequest.getName());
		mosaicService.save(mosaicRequest);
		return new GenericResponse("Mosaic " + mosaicRequest.getName() + " was saved");	
	}
	
	@PostMapping({"/delete/{mosaicName}"})
	public GenericResponse delete(@PathVariable("mosaicName") String mosaicName) {
		
		log.debug("Delete mosaic " + mosaicName);
		mosaicService.delete(mosaicName);
		return new GenericResponse("Mosaic " + mosaicName + " was deleted");
	}
	
	private void readAndWriteMosaic(String mosaicName, Integer columns, String boundary, OutputStream os) throws IOException {
		
		
		while(stopMosaic.get() != true) {
			
			byte mosaic[] = mosaicService.generateMosaic(mosaicName, columns);
				
			os.write(("--"+boundary+"\n").getBytes());
			os.write(("Content-Type: image/jpeg\n").getBytes());
			os.write(("Content-length: " +  mosaic.length + "\n\n").getBytes());
			os.write(mosaic);
			os.write("\n\n".getBytes());
				
			os.flush();
		}
	}

	@GetMapping("/get/{mosaicName}/{columns}")
	ResponseEntity<StreamingResponseBody> get(@PathVariable("mosaicName") String mosaicName,
			@PathVariable("columns") Integer columns) {
		
		log.debug("Get mosaic " + mosaicName + " with " + columns + " columns");
		stopMosaic.set(false);
		
		Random rnd = new Random();
		
		final String BOUNDARY = "---------------------------" +
				100000 + rnd.nextInt(900000) +
				100000 + rnd.nextInt(900000);
		
		return ResponseEntity.ok()
				.header("Content-Type", "multipart/x-mixed-replace; boundary="+BOUNDARY)
				.body( (os) -> { readAndWriteMosaic(mosaicName, columns, BOUNDARY, os); });
		
	}
	
	@GetMapping("/reload/{mosaicName}")
	public GenericResponse reloadMosaicConfiguration(@PathVariable("mosaicName") String mosaicName) {
		
		log.debug("Reload mosaic configuration " + mosaicName);
		stopMosaic.set(true);
		mosaicService.removeMosaicConfiguration(mosaicName);
		return new GenericResponse("Mosaic " + mosaicName + " reloaded");
	}
}
