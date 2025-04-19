package com.domain.repository;

import com.domain.dto.GithubRepositoryDto;
import com.domain.dto.IssueDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GithubRepository {
  Page<GithubRepositoryDto> getRepositories(List<String> languages, List<String> labels, Pageable pageable);
  Page<IssueDto> getIssuesForRepository(String repositoryId, Pageable pageable);
  List<String> findAllLanguage();

  void markAllIndexed(List<Long> repoIds);
}
