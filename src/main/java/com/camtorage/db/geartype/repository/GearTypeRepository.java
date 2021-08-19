package com.camtorage.db.geartype.repository;

import com.camtorage.entity.geartype.GearType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GearTypeRepository extends JpaRepository<GearType, Integer>, GearTypeRepositoryCustom {
}
