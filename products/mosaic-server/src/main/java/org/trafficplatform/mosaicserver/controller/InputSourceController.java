package org.trafficplatform.mosaicserver.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trafficplatform.mosaicserver.bean.api.response.GenericResponse;
import org.trafficplatform.mosaicserver.bean.api.response.InputSourceResponse;
import org.trafficplatform.mosaicserver.entity.InputSourceEntity;
import org.trafficplatform.mosaicserver.service.InputSourceService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("inputSource")
@Slf4j
public class InputSourceController extends BaseController {
	
	@Autowired
	private InputSourceService inputSourceService;
	
	@PostMapping({"/save"})
	public GenericResponse add(@Valid @RequestBody InputSourceEntity inputSource) {
		
		log.debug("Adding new input source " + inputSource.getName());
		
		inputSourceService.save(inputSource);
		
		return new GenericResponse("Input source " + inputSource.getName() + " saved");
	}
	
	@PostMapping({"/update"})
	public GenericResponse update(@Valid @RequestBody InputSourceEntity inputSource) {
		
		log.debug("Update input source " + inputSource.getName());
		
		inputSourceService.update(inputSource);
		
		return new GenericResponse("Input source " + inputSource.getName() + " updated");
	}
	
	@PostMapping({"/delete/{inputSourceName}"})
	public GenericResponse delete(@PathVariable("inputSourceName") String inputSourceName) {
		
		log.debug("Delete input source " + inputSourceName);
		
		inputSourceService.delete(inputSourceName);
		
		return new GenericResponse("Input source " + inputSourceName + " was deleted");
	}
	
	@GetMapping({"/list"})
	public List<InputSourceResponse> list() {
		
		log.debug("Get all input sources");
		return inputSourceService.findAll( t -> new  InputSourceResponse(t));
	}
}
