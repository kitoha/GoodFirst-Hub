package com.domain.repository;

import com.domain.dto.LabelDto;
import com.domain.entity.QLabelEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LabelRepositoryImpl implements LabelRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<LabelDto> findAllDistinctLabelName() {
    QLabelEntity label = QLabelEntity.labelEntity;
    return queryFactory
        .select(Projections.constructor(LabelDto.class,
            label.name,
            label.color.min()
        ))
        .from(label)
        .groupBy(label.name)
        .fetch();
  }
}
