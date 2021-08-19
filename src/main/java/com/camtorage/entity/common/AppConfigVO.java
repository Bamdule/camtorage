package com.camtorage.entity.common;

import com.camtorage.entity.geartype.GearTypeVO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppConfigVO {

    private List<GearTypeVO> gearTypes;
}
