package com.camtorage.entity.gear;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GearTO {
    private Integer id;

    private String name;

    private Integer gearTypeId;

    private String color;

    private String company;

    private String capacity;

    private Integer price = 0;

    private String buyDt;

}
