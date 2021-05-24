package org.trafficplatform.mosaicserver.service;

public interface Converter<S,T> {
	
	T transform(S s);

}
