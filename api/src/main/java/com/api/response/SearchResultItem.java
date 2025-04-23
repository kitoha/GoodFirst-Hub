package com.api.response;

public record SearchResultItem(
    ItemType  type,
    String repoId,
    String repoName,
    String matchedText
  //  String issueUrl // 검색 서비스 확장. 클릭 시 이슈 링크로 이동. (추후 업데이트)
) {
  public enum ItemType {
    REPOSITORY,
    ISSUE
  }
}
