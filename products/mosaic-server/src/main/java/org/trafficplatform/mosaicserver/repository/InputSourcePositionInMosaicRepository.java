package org.trafficplatform.mosaicserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trafficplatform.mosaicserver.entity.InputSourcePositionInMosaicEntity;

@Repository
public interface InputSourcePositionInMosaicRepository extends JpaRepository<InputSourcePositionInMosaicEntity, Long> {


}
