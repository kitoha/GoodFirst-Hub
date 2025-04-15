package com.api.service;

import com.domain.dto.LabelDto;
import com.domain.repository.LabelRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LabelService {

  private final LabelRepositoryImpl labelRepository;

  public List<LabelDto> getLabels() {
    return labelRepository.findAllDistinctLabelName();
  }

}
