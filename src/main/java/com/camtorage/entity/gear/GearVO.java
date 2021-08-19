package com.camtorage.entity.gear;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GearVO {
    private Integer id;

    private String name;

    private Integer gearTypeId;

    private String gearTypeName;

    private String color;

    private String company;

    private String capacity;

    private List<GearImageVO> gearImages;
}
