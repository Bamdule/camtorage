package com.camtorage.entity.gear;

import com.camtorage.entity.geartype.GearType;
import com.camtorage.entity.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String color;

    private String company;

    private String capacity;

    private Integer price = 0;

    private String buyDt;

    @Column(name = "gear_type_id", nullable = false)
    private Integer gearTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}