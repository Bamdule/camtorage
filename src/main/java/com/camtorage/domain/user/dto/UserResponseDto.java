package com.camtorage.domain.user.dto;

import com.camtorage.entity.user.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseDto {
    private Integer id;

    private String name = "";

    private String email = "";

    private String phone = "";

    private Integer userImageId = null;

    private String userImageUrl = "";

    private Boolean isPublic;

    private String aboutMe = "";

    @Getter
    public static class CreateResponse {
        private Integer id;

        private String email;

        private String name;

        private Boolean isPublic;

        @Builder
        public CreateResponse(Integer id, String email, String name, String phone, Boolean isPublic) {
            this.id = id;
            this.email = email;
            this.name = name;
            this.isPublic = isPublic;
        }

        public static UserResponseDto.CreateResponse of(UserInfo.Create createdUser) {
            return CreateResponse
                .builder()
                .id(createdUser.getId())
                .email(createdUser.getEmail())
                .name(createdUser.getName())
                .isPublic(createdUser.getIsPublic())
                .build();
        }
    }

}
