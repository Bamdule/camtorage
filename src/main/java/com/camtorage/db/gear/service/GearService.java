package com.camtorage.db.gear.service;

import com.camtorage.entity.gear.GearImageTO;
import com.camtorage.entity.gear.GearImageVO;
import com.camtorage.entity.gear.GearRequest;
import com.camtorage.entity.gear.GearResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GearService {

    public GearResponse saveGear(Integer userId, GearRequest gearRequest, List<MultipartFile> files);

    public void updateGear(Integer userId, Integer gearId, GearRequest gearRequest, List<GearImageTO> gearImages);

    public void deleteGear(Integer gearId);

    public List<GearResponse> getListGear(Integer userId);

    public List<GearImageVO> getListGearImage(Integer userId, Integer gearId);

    public long getCountGear(Integer userId);
}
