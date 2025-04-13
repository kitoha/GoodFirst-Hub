package com.batch.label.converter;

import com.batch.model.GitHubLabel;
import com.domain.entity.GitHubRepositoryEntity;
import com.domain.entity.LabelEntity;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelDtoConverter {

  public static LabelEntity convertToLabelEntity(GitHubLabel labelDto, GitHubRepositoryEntity repository){

    return LabelEntity.builder()
        .name(labelDto.getName())
        .color(labelDto.getColor())
        .repository(repository)
        .build();
  }
}
