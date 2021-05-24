package org.trafficplatform.videoserver.bean.api.response;

import org.trafficplatform.videoserver.entity.ConsolidatedVideoEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsolidateVideoResponse {
	
	private Long id;
	private String sourceName;
	private String start;
	private String end;
	
	
	public ConsolidateVideoResponse(ConsolidatedVideoEntity cve) {
		
		this.id = cve.getId();
		this.sourceName = cve.getVideoSource().getName();
		this.start = cve.getStart();
		this.end = cve.getEnd();
	}

}
