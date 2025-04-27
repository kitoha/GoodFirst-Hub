package com.api.response;

public record SearchResultItem(
    ItemType type,
    String repoId,
    String repoName,
    String matchedText,
    String issueUrl
) {
  public enum ItemType {
    REPOSITORY,
    ISSUE
  }
}
