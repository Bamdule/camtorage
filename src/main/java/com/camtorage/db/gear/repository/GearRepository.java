package com.camtorage.db.gear.repository;

import com.camtorage.entity.gear.Gear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GearRepository extends JpaRepository<Gear, Integer>, GearRepositoryCustom {

    public List<Gear> getAllByUserId(Integer userId);
}
