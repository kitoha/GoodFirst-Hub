package com.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GitHubReactions {
  @JsonProperty("total_count")
  private int totalCount;
}
