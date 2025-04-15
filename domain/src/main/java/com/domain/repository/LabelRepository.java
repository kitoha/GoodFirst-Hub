package com.domain.repository;

import com.domain.dto.LabelDto;
import java.util.List;

public interface LabelRepository {

  List<LabelDto> findAllDistinctLabelName();

}
