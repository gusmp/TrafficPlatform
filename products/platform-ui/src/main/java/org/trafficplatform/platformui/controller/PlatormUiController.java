package org.trafficplatform.platformui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trafficplatform.platformui.bean.api.response.GenericResponse;
import org.trafficplatform.platformui.service.PlatormUiService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("test")
@Slf4j
public class PlatormUiController extends BaseController {
	
	@Autowired
	private PlatormUiService platormUiService;
	
	// http://localhost:9095/test/test
	@GetMapping({"/test"})
	public GenericResponse test() {
		
		log.debug("PlatormUiController test");
		platormUiService.test();
		return new GenericResponse("test");	
	}

}
