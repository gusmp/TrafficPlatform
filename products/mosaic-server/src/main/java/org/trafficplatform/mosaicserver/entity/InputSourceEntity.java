package org.trafficplatform.mosaicserver.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "INPUT_SOURCE")
@Getter
@Setter
public class InputSourceEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Size(min = 4)
	private String name;
	
	@Size(min = 1, max = 250)
	private String url;
	
	@OneToMany(mappedBy = "inputSource")
	Set<InputSourcePositionInMosaicEntity> mosaicSet;
			

}
