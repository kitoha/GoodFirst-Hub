package com.batch.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IssueRecord {
  private int issueNumber;
  private String title;
  private int commentCount;
  private String url;
  private LocalDateTime createdAt;
}
