package com.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GitHubIssueItem {
  private int number;
  private String title;
  private String body;

  @JsonProperty("html_url")
  private String htmlUrl;

  @JsonProperty("created_at")
  private String createdAt;

  @JsonProperty("updated_at")
  private String updatedAt;

  @JsonProperty("comments")
  private int commentCnt;

  private GitHubUser user;

  private List<GitHubUser> assignees = new ArrayList<>();

  private List<GitHubLabel> labels = new ArrayList<>();

  @JsonProperty("pull_request")
  private JsonNode pullRequest;

  private GitHubReactions reactions;
}
