package com.camtorage.entity.version;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.camtorage.entity.common.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "app_version")
@Entity
public class AppVersion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "minimum_version", nullable = false)
    private String minimumVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "device", nullable = false, unique = true)
    private Device device;

    public void updateAppVersion(Device device, String minimumVersion) {
        this.device = device;
        this.minimumVersion = minimumVersion;
    }
}
