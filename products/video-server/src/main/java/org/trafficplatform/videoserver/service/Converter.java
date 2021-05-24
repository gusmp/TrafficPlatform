package org.trafficplatform.videoserver.service;

public interface Converter<S,T> {
	
	T transform(S s);

}
