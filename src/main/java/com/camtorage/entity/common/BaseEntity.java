package com.camtorage.entity.common;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

    @CreatedDate
    @ColumnDefault("current_timestamp()")
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @ColumnDefault("current_timestamp()")
    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
