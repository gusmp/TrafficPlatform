package org.trafficplatform.mosaicserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trafficplatform.mosaicserver.entity.MosaicEntity;

@Repository
public interface MosaicRepository extends JpaRepository<MosaicEntity, Long> {
	
	MosaicEntity findByName(String name);

}
