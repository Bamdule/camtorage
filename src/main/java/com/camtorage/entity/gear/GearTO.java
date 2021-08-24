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

    private Integer price;

    public Gear getGear() {
        return Gear.builder()
                .id(this.id)
                .name(this.name)
                .gearTypeId(this.gearTypeId)
                .color(this.color)
                .capacity(this.capacity)
                .company(this.company)
                .price(this.price)
                .build();
    }

}
