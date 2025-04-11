package com.batch.model;

import java.util.List;
import lombok.Getter;

@Getter
public class GitHubRepositorySearchResponse {
  private List<GitHubRepositoryItem> items;
}
