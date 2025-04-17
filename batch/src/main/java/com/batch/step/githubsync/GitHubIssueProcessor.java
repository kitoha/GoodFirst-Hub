package com.batch.step.githubsync;

import com.batch.label.service.LabelNormalizer;
import com.batch.model.GitHubLabel;
import com.batch.model.IssueRecord;
import com.batch.model.RepositoryRecord;
import com.batch.label.service.LabelClassifier;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class GitHubIssueProcessor implements ItemProcessor<RepositoryRecord, RepositoryRecord> {

  private final LabelNormalizer labelNormalizer;
  @Override
  public RepositoryRecord process(RepositoryRecord item) throws Exception {

    if (item.getLanguage() == null) return null;

    List<IssueRecord> filteredIssues = item.getIssues().stream()
        .map(this::filterValidLabelsOnly)
        .filter(Objects::nonNull)
        .toList();

    if (filteredIssues.isEmpty()) return null;

    item.setIssues(filteredIssues);
    return item;
  }

  private IssueRecord filterValidLabelsOnly(IssueRecord issue) {
    List<GitHubLabel> validLabels = labelNormalizer.normalize(issue.getLabels());

    if (validLabels.isEmpty()) return null;

    issue.setLabels(validLabels);
    return issue;
  }
}
