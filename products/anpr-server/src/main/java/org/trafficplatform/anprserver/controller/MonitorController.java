package org.trafficplatform.anprserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("monitor")
@Slf4j
public class MonitorController extends BaseController {
	
	@GetMapping("/test")
	public String test() {
		
		log.debug("Monitor");
		return "OK";
	}
}
