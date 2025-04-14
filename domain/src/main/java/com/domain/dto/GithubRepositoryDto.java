package com.domain.dto;

import com.domain.entity.GitHubRepositoryEntity;
import com.domain.util.TsidUtil;
import java.util.List;

public record GithubRepositoryDto(
    String id,
    String name,
    String owner,
    String address,
    String primaryLanguage,
    int startCount,
    long issueCount
){
  public static GithubRepositoryDto from(GitHubRepositoryEntity entity, long issueCount) {
    return new GithubRepositoryDto(
        TsidUtil.encode(entity.getId()),
        entity.getName(),
        entity.getOwner(),
        entity.getAddress(),
        entity.getPrimaryLanguage(),
        entity.getStarCount(),
        issueCount
    );
  }
}
