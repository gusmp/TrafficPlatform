package org.trafficplatform.videoserver.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CONSOLIDATED_VIDEO")
@Getter
@Setter
public class ConsolidatedVideoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String start;
	
	private String end;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_source_id", nullable = false)
	private VideoSourceEntity videoSource;
}
