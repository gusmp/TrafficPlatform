package org.trafficplatform.videoserver.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.trafficplatform.videoserver.entity.ConsolidatedVideoEntity;

@Repository
public interface VideoConsolidateRepository extends JpaRepository<ConsolidatedVideoEntity, Long> {
	
	@Query("SELECT cv FROM ConsolidatedVideoEntity cv WHERE cv.videoSource.name= :sourceName")
	List<ConsolidatedVideoEntity> findBySourceName(String sourceName);

}
