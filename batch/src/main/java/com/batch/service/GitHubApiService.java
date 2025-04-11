package com.batch.service;

import com.batch.model.GitHubIssueItem;
import com.batch.model.GitHubRepositoryItem;
import com.batch.model.GitHubRepositorySearchResponse;
import com.batch.model.IssueRecord;
import com.domain.external.ApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubApiService {

  private final ApiClient apiClient;

  public List<GitHubRepositoryItem> fetchHighStarRepo(int page){
    String url =  "https://api.github.com/search/repositories?q=stars:>1000&sort=stars&order=desc&page=" + page;
    HttpHeaders headers = apiClient.getHeader();

    ResponseEntity<GitHubRepositorySearchResponse> response = apiClient.get(url, headers, GitHubRepositorySearchResponse.class);

    return Objects.requireNonNull(response.getBody()).getItems();

  }

  public List<GitHubIssueItem> fetchIssues(String owner, String repo){
    String url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues?state=open&per_page=30";
    HttpHeaders headers = apiClient.getHeader();
    ResponseEntity<GitHubIssueItem[]> response = apiClient.get(url, headers, GitHubIssueItem[].class);
    GitHubIssueItem[] items = response.getBody();

    return Arrays.stream(items).filter(item -> item.getPullRequest() == null).collect(Collectors.toList());
  }

}
