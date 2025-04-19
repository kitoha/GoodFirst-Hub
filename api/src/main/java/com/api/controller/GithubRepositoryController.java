package com.api.controller;

import com.api.service.GithubRepositoryService;
import com.domain.dto.GithubRepositoryDto;
import com.domain.dto.IssueDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/repositories")
@RequiredArgsConstructor
public class GithubRepositoryController {

  private final GithubRepositoryService githubRepositoryService;

  @GetMapping("")
  public ResponseEntity<Page<GithubRepositoryDto>> getRepositories(
      @RequestParam(required = false) List<String> language,
      @RequestParam(required = false) List<String> label,
      Pageable pageable) {
    return ResponseEntity.ok(githubRepositoryService.getRepositories(language, label, pageable));
  }

  @GetMapping("/{id}/issues")
  public ResponseEntity<Page<IssueDto>> getIssuesForRepository(
      @PathVariable("id") String repositoryId, Pageable pageable) {
    return ResponseEntity.ok(
        githubRepositoryService.getIssuesForRepository(repositoryId, pageable));
  }


}
