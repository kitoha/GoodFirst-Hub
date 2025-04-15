package com.api.service;

import com.domain.dto.LabelDto;
import com.domain.entity.LabelEntity;
import com.domain.repository.LabelJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelService {

  private final LabelJpaRepository labelJpaRepository;

  public List<LabelDto> getLabels() {
    List<LabelEntity> labelEntities = labelJpaRepository.findAll();

    return labelEntities.stream().map(
        labelEntity -> new LabelDto(labelEntity.getId(), labelEntity.getName(),
            labelEntity.getColor())).toList();
  }

}
