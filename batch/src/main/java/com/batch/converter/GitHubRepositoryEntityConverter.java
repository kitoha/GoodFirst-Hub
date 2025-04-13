package com.batch.converter;

import com.batch.model.RepositoryRecord;
import com.domain.entity.GitHubRepositoryEntity;
import com.domain.util.TsidUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GitHubRepositoryEntityConverter {
  public static GitHubRepositoryEntity convertToGitHubRepositoryEntity(RepositoryRecord record) {
    return GitHubRepositoryEntity.builder()
        .id(TsidUtil.generate())
        .name(record.getRepositoryName())
        .owner(record.getOwner())
        .address(record.getAddress())
        .primaryLanguage(record.getLanguage())
        .starCount(record.getStarCount())
        .build();
  }
}
