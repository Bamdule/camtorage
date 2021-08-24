package com.camtorage.db.gear.repository;

import com.camtorage.entity.gear.GearImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GearImageRepository extends JpaRepository<GearImage, Integer> {

    public List<GearImage> findAllByGearId(Integer gearId);

//    public void deleteAllByGearId(Integer gearId);
}
