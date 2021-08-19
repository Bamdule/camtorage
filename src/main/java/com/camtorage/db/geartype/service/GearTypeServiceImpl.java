package com.camtorage.db.geartype.service;

import com.camtorage.db.geartype.repository.GearTypeRepository;
import com.camtorage.entity.geartype.GearTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GearTypeServiceImpl implements GearTypeService {

    @Autowired
    private GearTypeRepository gearTypeRepository;


    @Override
    public List<GearTypeVO> getListGearType() {
        return gearTypeRepository.getListGearType();
    }
}
