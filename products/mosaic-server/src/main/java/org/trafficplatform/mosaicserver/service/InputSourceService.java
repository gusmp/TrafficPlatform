package org.trafficplatform.mosaicserver.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trafficplatform.mosaicserver.entity.InputSourceEntity;
import org.trafficplatform.mosaicserver.repository.InputSourceRepository;

@Service
@Transactional
public class InputSourceService {
	
	@Autowired
	private InputSourceRepository inputSourceRepository;
	
	public InputSourceEntity save(InputSourceEntity inputSource) {
		
		if (inputSourceRepository.findByName(inputSource.getName()) != null) {
			throw new RuntimeException("Input source name " + inputSource.getName() + " exists");
		}
		
		return inputSourceRepository.save(inputSource);
	}
	
	public InputSourceEntity update(InputSourceEntity inputSource) {
		
		InputSourceEntity isBbdd = inputSourceRepository.findByName(inputSource.getName());
		if (isBbdd == null) {
			throw new RuntimeException("Input source name " + inputSource.getName() + " does not exist");
		}
		
		inputSource.setId(isBbdd.getId());
		
		return inputSourceRepository.save(inputSource);
	}
	
	public void delete(String inputSourceName) {
		
		InputSourceEntity isBbdd = inputSourceRepository.findByName(inputSourceName);
		if (isBbdd == null) {
			throw new RuntimeException("Input source name " + inputSourceName + " does not exist");
		}
		
		inputSourceRepository.deleteById(isBbdd.getId());
	}
	
	public <T> List<T> findAll(Converter<InputSourceEntity, T> converter) {
		
		List<InputSourceEntity> inputSourceList = inputSourceRepository.findAll();
		List<T> inputSoureResponseList = new ArrayList<T>();
		
		for(InputSourceEntity ise : inputSourceList) {
			inputSoureResponseList.add(converter.transform(ise));
		}
		
		return inputSoureResponseList;
		
	}
}
