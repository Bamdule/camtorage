package com.camtorage.entity.gear;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GearRequest {
    //    private Integer id;

    @NotBlank(message = "장비 명은 필수 값입니다.")
    private String name;

    @NotNull(message = "장비 타입은 필수 값입니다.")
    private Integer gearTypeId;

    @NotEmpty
    private String color;
    @NotEmpty
    private String company;
    @NotEmpty
    private String capacity;

    @NotNull
    private Integer price = 0;
    @NotEmpty
    private String buyDt;

    private String description = "";

}
