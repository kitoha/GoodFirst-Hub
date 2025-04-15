package com.batch.step;

import static com.batch.converter.GitHubRepositoryEntityConverter.convertToGitHubRepositoryEntity;
import static com.batch.converter.IssueDtoConverter.convertToIssueEntity;
import static com.batch.label.converter.LabelDtoConverter.convertToLabelEntity;

import com.batch.model.GitHubLabel;
import com.batch.model.IssueRecord;
import com.batch.model.RepositoryRecord;
import com.domain.entity.GitHubRepositoryEntity;
import com.domain.entity.IssueEntity;
import com.domain.entity.IssueLabelEntity;
import com.domain.entity.LabelEntity;
import com.domain.repository.GithubJpaRepository;
import com.domain.repository.IssueJpaRepository;
import com.domain.repository.LabelJpaRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class GitHubIssueWriter implements ItemWriter<RepositoryRecord> {

  private final GithubJpaRepository githubJpaRepository;
  private final IssueJpaRepository issueJpaRepository;
  private final LabelJpaRepository labelJpaRepository;

  @Override
  public void write(Chunk<? extends RepositoryRecord> chunk) throws Exception {
    for (RepositoryRecord record : chunk.getItems()) {
      GitHubRepositoryEntity savedRepository = saveRepository(record);
      saveIssues(record.getIssues(), savedRepository);
    }
  }

  private void saveIssues(List<IssueRecord> issueRecords, GitHubRepositoryEntity repository) {
    for (IssueRecord issueRecord : issueRecords) {
      Optional<IssueEntity> issueEntity = issueJpaRepository.findByRepositoryAndUrl(repository, issueRecord.getHtmlUrl());
      if(issueEntity.isEmpty()) {
        IssueEntity issue = convertToIssueEntity(issueRecord, repository);
        saveLabels(issueRecord.getLabels(), issue, repository);
        issueJpaRepository.save(issue);
      }
    }
  }

  private void saveLabels(List<GitHubLabel> labels, IssueEntity issue, GitHubRepositoryEntity repository) {
    Set<String> addedLabelNames = new HashSet<>();

    for (GitHubLabel labelDto : labels) {
      if (addedLabelNames.contains(labelDto.getName())) continue;
      LabelEntity label = saveOrGetLabel(labelDto, repository);
      IssueLabelEntity issueLabel = IssueLabelEntity.builder()
          .issue(issue)
          .label(label)
          .build();
      issue.getIssuelabel().add(issueLabel);
      addedLabelNames.add(labelDto.getName());
    }


  }

  private LabelEntity saveOrGetLabel(GitHubLabel labelDto, GitHubRepositoryEntity repository) {
    return labelJpaRepository.findByRepositoryAndName(repository, labelDto.getName())
        .orElseGet(() -> labelJpaRepository.save(convertToLabelEntity(labelDto,repository)));
  }

  private GitHubRepositoryEntity saveRepository(RepositoryRecord record) {
    Optional<GitHubRepositoryEntity> optionalGitHubRepository = githubJpaRepository.findByNameAndOwner(
        record.getRepositoryName(), record.getOwner());

    if(optionalGitHubRepository.isPresent()){
      return optionalGitHubRepository.get();
    }else{
      GitHubRepositoryEntity gitHubRepositoryEntity = convertToGitHubRepositoryEntity(record);
      return githubJpaRepository.save(gitHubRepositoryEntity);
    }
  }
}
