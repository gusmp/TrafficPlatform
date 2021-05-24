package org.trafficplatform.videoserver.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.trafficplatform.videoserver.service.video.capture.VideoCaptureType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "VIDEO_SOURCE")
@Getter
@Setter
public class VideoSourceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private boolean enableVideoSource;
	
	@NotNull
	private boolean enableSaveVideo;
	
	@NotEmpty
	@Size(min = 4)
	private String name;
	
	@Size(min = 1, max = 250)
	private String url;
	
	@NotNull
	private boolean removeOldData;
	
	@Positive
	private Integer numberOfDaysToPreserve;
	
	@NotEmpty
	@Size(min = 5, max =  255)
	private String rootCapturePath;
	
	@Size(min = 5, max =  255)
	private String rootConsolidatedPath;
	
	//@Size(min = 5)
	private VideoCaptureType videoCaptureType;
	
	@OneToMany(mappedBy = "videoSource",cascade = CascadeType.REMOVE)
	private Set<ConsolidatedVideoEntity> consolidatedVideos;
	
	
}
