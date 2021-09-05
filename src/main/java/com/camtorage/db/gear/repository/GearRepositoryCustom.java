package com.camtorage.db.gear.repository;

import com.camtorage.entity.gear.GearImageVO;
import com.camtorage.entity.gear.GearVO;

import java.util.List;

public interface GearRepositoryCustom {

    public List<GearImageVO> getListGearImage(Integer gearId);

    public List<GearVO> getListGear(Integer userId);

    public long getCountGear(Integer userId);

}
