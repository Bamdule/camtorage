package com.camtorage.entity.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultVO {
    private String status;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;

}
