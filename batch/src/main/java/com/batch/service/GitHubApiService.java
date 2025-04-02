package com.batch.service;

import com.batch.common.Constants;
import com.batch.model.GitHubIssueItem;
import com.batch.model.GitHubIssuesResponse;
import com.batch.model.IssueRecord;
import com.domain.external.ApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GitHubApiService {

  private final ApiClient apiClient;

  public List<IssueRecord> fetchIssues(int page){

    String query = Constants.SEARCH_GOOD_FIRST_ISSUE + "+" +
        Constants.SEARCH_JAVA_LANGUAGE + "+" +
        Constants.SEARCH_STATE_OPEN;

    URI uri = UriComponentsBuilder.fromHttpUrl(Constants.SEARCH_API_PREFIX)
        .queryParam("q", query)
        .queryParam("per_page", 30)
        .queryParam("page", page)
        .build().encode().toUri();

    HttpHeaders headers = apiClient.getHeader();

    ResponseEntity<JsonNode> response = apiClient.get(uri.toString(), headers, JsonNode.class);
    int a = 1;
//    GitHubIssuesResponse issuesResponse = response.getBody();
//    List<IssueRecord> issues = new ArrayList<>();
//
//    if (issuesResponse != null && issuesResponse.getItems() != null) {
//      for (GitHubIssueItem item : issuesResponse.getItems()) {
//        LocalDateTime createdAt = LocalDateTime.parse(item.getCreatedAt(), DateTimeFormatter.ISO_DATE_TIME);
//        IssueRecord issue = IssueRecord.builder()
//            .issueNumber(item.getNumber())
//            .title(item.getTitle())
//            .createdAt(createdAt)
//            .commentCount(item.getComments())
//            .url(item.getHtmlUrl())
//            .build();
//
//        issues.add(issue);
//      }
//    }
//
//    return issues;
    return null;
  }

}
