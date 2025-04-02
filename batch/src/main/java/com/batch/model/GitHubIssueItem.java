package com.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubIssueItem {
  private int number;
  private String title;

  @JsonProperty("created_at")
  private String createdAt;

  private int comments;

  @JsonProperty("html_url")
  private String htmlUrl;

}
