package org.trafficplatform.platformui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trafficplatform.platformui.entity.PlatormUiEntity;

@Repository
public interface PlatormUiRepository extends JpaRepository<PlatormUiEntity, Long> {
	
	PlatormUiEntity findByName(String name);

}
