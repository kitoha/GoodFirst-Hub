package com.batch.converter;

import com.batch.model.GitHubIssueItem;
import com.batch.model.IssueRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueItemConverter {
  public static IssueRecord convertToRecord(GitHubIssueItem item){
    return IssueRecord.builder()
        .number(item.getNumber())
        .title(item.getTitle())
        .body(item.getBody())
        .htmlUrl(item.getHtmlUrl())
        .createdAt(item.getCreatedAt())
        .updatedAt(item.getUpdatedAt())
        .commentCount(item.getCommentCnt())
        .issuer(item.getAssignees().getFirst().getLogin())
        .labels(item.getLabels())
        .reactionCount(item.getReactions().getTotalCount())
        .build();
  }
}
