package org.trafficplatform.mosaicserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trafficplatform.mosaicserver.entity.InputSourceEntity;

@Repository
public interface InputSourceRepository extends JpaRepository<InputSourceEntity, Long> {
	
	InputSourceEntity findByName(String name);

}
