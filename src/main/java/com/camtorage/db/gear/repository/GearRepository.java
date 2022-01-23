package com.camtorage.db.gear.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.camtorage.entity.gear.Gear;

public interface GearRepository extends JpaRepository<Gear, Integer>, GearRepositoryCustom {

    public List<Gear> getAllByUserId(Integer userId);
}
