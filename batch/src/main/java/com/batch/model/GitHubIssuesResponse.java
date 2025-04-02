package com.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubIssuesResponse {

  @JsonProperty("total_count")
  private int totalCount;
  @JsonProperty("incomplete_results")
  private boolean isComplete;
  private List<GitHubIssueItem> items;

}
