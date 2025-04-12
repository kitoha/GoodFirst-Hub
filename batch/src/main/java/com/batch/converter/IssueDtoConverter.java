package com.batch.converter;

import com.batch.model.GitHubIssueItem;
import com.batch.model.IssueRecord;
import com.domain.entity.GitHubRepositoryEntity;
import com.domain.entity.IssueEntity;
import com.domain.util.TsidUtil;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueDtoConverter {
  public static IssueRecord convertToRecord(GitHubIssueItem item){
    String issuer = item.getAssignees().isEmpty()? null: item.getAssignees().getFirst().getLogin();

    return IssueRecord.builder()
        .number(item.getNumber())
        .title(item.getTitle())
        .body(item.getBody())
        .htmlUrl(item.getHtmlUrl())
        .createdAt(item.getCreatedAt())
        .updatedAt(item.getUpdatedAt())
        .commentCount(item.getCommentCnt())
        .issuer(issuer)
        .labels(item.getLabels())
        .reactionCount(item.getReactions().getTotalCount())
        .build();
  }

  public static IssueEntity convertToIssueEntity(IssueRecord issueRecord, GitHubRepositoryEntity repository){
    LocalDateTime createdAt = OffsetDateTime.parse(issueRecord.getCreatedAt()).toLocalDateTime();
    LocalDateTime updatedAt =  OffsetDateTime.parse(issueRecord.getUpdatedAt()).toLocalDateTime();
    String truncatedTitle = truncateTitle(issueRecord.getTitle());

    return IssueEntity.builder()
        .id(TsidUtil.generate())
        .issueNumber(issueRecord.getNumber())
        .title(truncatedTitle)
        .url(issueRecord.getHtmlUrl())
        .issuer(issueRecord.getIssuer())
        .commentCount(issueRecord.getCommentCount())
        .reactionCount(issueRecord.getReactionCount())
        .cratedAt(createdAt)
        .updatedAt(updatedAt)
        .repository(repository)
        .build();
  }

  public static String truncateTitle(String title) {
    int maxLength = 300;
    if (title != null && title.length() > maxLength) {
      return title.substring(0, maxLength - 3) + "...";
    }
    return title;
  }
}
