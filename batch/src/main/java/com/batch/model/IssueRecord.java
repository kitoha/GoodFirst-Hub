package com.batch.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IssueRecord {
  private int number;
  private String title;
  private String body;
  private String htmlUrl;
  private String createdAt;
  private String updatedAt;
  private int commentCount;
  private String issuer;
  private List<String> assignedUser;
  private List<GitHubLabel> labels;
  private int reactionCount;
}
