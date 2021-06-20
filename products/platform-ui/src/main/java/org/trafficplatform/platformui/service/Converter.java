package org.trafficplatform.platformui.service;

public interface Converter<S,T> {
	
	T transform(S s);

}
