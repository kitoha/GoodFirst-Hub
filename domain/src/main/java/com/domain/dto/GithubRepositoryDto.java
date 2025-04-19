package com.domain.dto;

import com.domain.util.TsidUtil;

public record GithubRepositoryDto(
    String id,
    String name,
    String owner,
    String address,
    String primaryLanguage,
    int startCount,
    long issueCount
){
  public GithubRepositoryDto(
      Long rawId,
      String name,
      String owner,
      String address,
      String primaryLanguage,
      int startCount,
      long issueCount
  ) {
    this(
        TsidUtil.encode(rawId),
        name,
        owner,
        address,
        primaryLanguage,
        startCount,
        issueCount
    );
  }
}
