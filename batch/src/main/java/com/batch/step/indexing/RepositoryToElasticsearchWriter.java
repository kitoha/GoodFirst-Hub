package com.batch.step.indexing;

import com.domain.document.GitHubRepositoryDocument;
import com.domain.entity.GitHubRepositoryEntity;
import com.domain.entity.IssueEntity;
import com.domain.repository.GithubRepository;
import com.domain.repository.IssueJpaRepository;
import com.domain.repository.elasticsearch.GitHubEsRepository;
import com.domain.util.TsidUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class RepositoryToElasticsearchWriter implements ItemWriter<GitHubRepositoryEntity> {

  private final IssueJpaRepository issueJpaRepository;
  private final GithubRepository githubRepository;
  private final GitHubEsRepository gitHubEsRepository;

  public RepositoryToElasticsearchWriter(IssueJpaRepository issueJpaRepository,
      GithubRepository githubRepository, GitHubEsRepository gitHubEsRepository) {
    this.issueJpaRepository = issueJpaRepository;
    this.githubRepository = githubRepository;
    this.gitHubEsRepository = gitHubEsRepository;
  }

  @Override
  public void write(Chunk<? extends GitHubRepositoryEntity> chunk) throws Exception {
    if(chunk.getItems().isEmpty()){
      return ;
    }

    List<GitHubRepositoryEntity> repositoryEntities = new ArrayList<>(chunk.getItems());

    List<Long> repoIds = repositoryEntities.stream()
        .map(GitHubRepositoryEntity::getId)
        .toList();

    List<IssueEntity> issueEntities = issueJpaRepository.findByRepositoryIdIn(repoIds);

    Map<Long, List<IssueEntity>> issueMap = issueEntities.stream()
        .collect(Collectors.groupingBy(issue -> issue.getRepository().getId()));

    List<GitHubRepositoryDocument> documents = repositoryEntities.stream()
        .map(repo -> getGitHubRepositoryDocument(repo, issueMap.getOrDefault(repo.getId(), Collections.emptyList())))
        .toList();

    gitHubEsRepository.saveAll(documents);

    githubRepository.markAllIndexed(repoIds);
  }

  private GitHubRepositoryDocument getGitHubRepositoryDocument(GitHubRepositoryEntity repositoryEntities, List<IssueEntity> issues){
    List<String> titles = issues.stream()
        .map(IssueEntity::getTitle)
        .collect(Collectors.toList());

    String repoId = TsidUtil.encode(repositoryEntities.getId());
    return GitHubRepositoryDocument.builder()
        .id(repoId)
        .name(repositoryEntities.getName())
        .owner(repositoryEntities.getOwner())
        .address(repositoryEntities.getAddress())
        .primaryLanguage(repositoryEntities.getPrimaryLanguage())
        .starCount(repositoryEntities.getStarCount())
        .issueTitles(titles)
        .build();
  }
}
