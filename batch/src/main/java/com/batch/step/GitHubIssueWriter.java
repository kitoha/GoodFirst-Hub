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
import java.util.List;
import java.util.Optional;
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
      IssueEntity issue = convertToIssueEntity(issueRecord, repository);
      issueJpaRepository.save(issue);
      saveLabels(issueRecord.getLabels(), issue, repository);
    }
  }

  private void saveLabels(List<GitHubLabel> labels, IssueEntity issue, GitHubRepositoryEntity repository) {
    for (GitHubLabel labelDto : labels) {
      LabelEntity label = saveOrGetLabel(labelDto, repository);
      IssueLabelEntity issueLabel = IssueLabelEntity.builder()
          .issue(issue)
          .label(label)
          .build();
      issue.getIssuelabel().add(issueLabel);
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
