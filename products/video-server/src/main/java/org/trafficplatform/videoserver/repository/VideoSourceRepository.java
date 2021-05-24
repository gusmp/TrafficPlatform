package org.trafficplatform.videoserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trafficplatform.videoserver.entity.VideoSourceEntity;


@Repository
public interface VideoSourceRepository extends JpaRepository<VideoSourceEntity, Long> {
	
	List<VideoSourceEntity> findByEnableVideoSource(Boolean enabled);
	
	VideoSourceEntity findByName(String name);
	

}
