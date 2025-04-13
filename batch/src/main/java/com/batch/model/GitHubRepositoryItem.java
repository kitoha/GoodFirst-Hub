package com.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GitHubRepositoryItem {
  @JsonProperty("name")
  private String repositoryName;
  @JsonProperty("owner")
  private GitHubOwner owner;
  @JsonProperty("stargazers_count")
  private int starCount;
  @JsonProperty("html_url")
  private String htmlUrl;
  @JsonProperty("language")
  private String language;
}
