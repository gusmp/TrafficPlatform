package org.trafficplatform.anprserver.service;

public interface Converter<S,T> {
	
	T transform(S s);

}
