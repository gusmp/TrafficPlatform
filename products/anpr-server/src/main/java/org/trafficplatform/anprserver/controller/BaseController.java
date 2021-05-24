package org.trafficplatform.anprserver.controller;


import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.trafficplatform.anprserver.bean.api.response.GenericResponse;

public abstract class BaseController {
	
	
	@ExceptionHandler({ RuntimeException.class, MethodArgumentNotValidException.class })
	public GenericResponse handleException(Exception ex) {
		
		if (ex instanceof MethodArgumentNotValidException) {
			
			String msg = "";
			for(ObjectError error : ((MethodArgumentNotValidException) ex).getAllErrors()) {
				
				String fieldName = ((FieldError) error).getField();
				msg += fieldName + " " +  error.getDefaultMessage() + "\n";
			}
			
			return new GenericResponse(false, -1, msg);
			
		} else {
			return new GenericResponse(false, -1, ex.getMessage());
		}
	}
}
