package org.trafficplatform.anprserver.controller;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.trafficplatform.anprserver.bean.api.response.PlateInfo;
import org.trafficplatform.anprserver.service.AnprService;
import org.trafficplatform.anprserver.service.plataInfoProviders.PlateInfoProvider;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("anpr")
@Slf4j
public class AnprController extends BaseController {
	
	@Autowired
	private AnprService anprService;
	
	@PostMapping("/get/{plateInfoProvider}")
	public List<PlateInfo> getPlateInfo(@PathVariable("plateInfoProvider") PlateInfoProvider plateInfoProvider, 
			@RequestParam MultipartFile imageFile) {
		
		log.debug("Request plate info provider " + plateInfoProvider.name());
		
		log.debug(imageFile.getName());
		
		if (imageFile.isEmpty() == true) {
			log.debug("The plate image is missing");
			throw new InvalidParameterException("There was no image in  the request to find a plate");
		}
		
		return anprService.getPlateInfo(plateInfoProvider, imageFile.getResource());
	} 
}
