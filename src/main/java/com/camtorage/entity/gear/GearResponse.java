package com.camtorage.entity.gear;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GearResponse {
    private Integer id;

    private String name;

    private Integer gearTypeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String gearTypeName;

    private String color;

    private String company;

    private String capacity;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer price = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String buyDt;
    private String description;

    private LocalDateTime createDt;

    private LocalDateTime updateDt;

}
