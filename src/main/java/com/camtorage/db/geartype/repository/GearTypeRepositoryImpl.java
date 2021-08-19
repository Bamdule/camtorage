package com.camtorage.db.geartype.repository;

import com.camtorage.entity.geartype.GearTypeVO;
import com.camtorage.entity.geartype.QGearType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

import static com.camtorage.entity.geartype.QGearType.gearType;

public class GearTypeRepositoryImpl implements GearTypeRepositoryCustom {

    @Autowired
    private EntityManager em;

    @Override
    public List<GearTypeVO> getListGearType() {
        JPAQueryFactory query = new JPAQueryFactory(em);

        return query
                .select(Projections.bean(
                        GearTypeVO.class,
                        gearType.id,
                        gearType.name
                ))
                .from(gearType)
                .orderBy(gearType.seq.asc())
                .fetch()
                ;
    }
}
