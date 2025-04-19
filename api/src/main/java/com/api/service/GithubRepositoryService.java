package com.api.service;

import com.domain.dto.GithubRepositoryDto;
import com.domain.dto.IssueDto;
import com.domain.repository.GithubRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubRepositoryService {

  private final GithubRepositoryImpl githubRepository;

  public Page<GithubRepositoryDto> getRepositories(List<String> language, List<String> label, Pageable pageable) {
    return githubRepository.getRepositories(language, label, pageable);
  }

  public Page<IssueDto> getIssuesForRepository(String repositoryId, Pageable pageable){
    return githubRepository.getIssuesForRepository(repositoryId, pageable);
  }
}
