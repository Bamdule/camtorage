package com.camtorage.entity.user;

import com.camtorage.entity.image.Image;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    private Boolean isPublic = true;

    @Column(name = "join_dt", updatable = false)
    private LocalDateTime joinDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_image_id")
    private Image image;

    @Column(name = "user_image_id", updatable = false, insertable = false)
    private Integer userImageId;

}
