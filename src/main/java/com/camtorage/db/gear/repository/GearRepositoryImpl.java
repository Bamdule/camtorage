package com.camtorage.db.gear.repository;

import com.camtorage.entity.gear.GearImageVO;
import com.camtorage.entity.gear.GearResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static com.camtorage.entity.gear.QGear.gear;
import static com.camtorage.entity.gear.QGearImage.gearImage;
import static com.camtorage.entity.geartype.QGearType.gearType;

public class GearRepositoryImpl implements GearRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public List<GearImageVO> getListGearImage(Integer gearId) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        return query
                .select(Projections.bean(
                        GearImageVO.class,
                        gearImage.image.id.as("imageId"),
                        gearImage.image.orgFilename,
                        gearImage.image.path.as("url")
                ))
                .from(gearImage)
                .leftJoin(gearImage.image)
                .where(gearImage.gear.id.eq(gearId))
                .orderBy(gearImage.id.asc())
                .fetch()
                ;
    }

    @Override
    public List<GearResponse> getListGear(Integer userId) {

        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(Projections.bean(
                GearResponse.class,
                gear.id,
                gear.capacity,
                gear.color,
                gear.company,
                gear.name,
                gear.price.coalesce(0).as("price"),
                gear.buyDt,
                gearType.id.as("gearTypeId"),
                gearType.name.as("gearTypeName")
        ))
                .from(gear)
                .leftJoin(gearType).on(gearType.id.eq(gear.gearTypeId))
                .where(gear.user.id.eq(userId))
                .fetch();
    }

    @Override
    public long getCountGear(Integer userId) {
        JPAQueryFactory query = new JPAQueryFactory(em);

        return query.select(gear)
                .from(gear)
                .where(gear.user.id.eq(userId))
                .fetchCount();
    }
}
