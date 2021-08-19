package com.camtorage.db.gear.service;

import com.camtorage.entity.gear.GearTO;
import com.camtorage.entity.gear.GearVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GearService {

    public void saveGear(Integer userId, GearTO gearTO, List<MultipartFile> files);

    public List<GearVO> getListGear(Integer userId);
}
