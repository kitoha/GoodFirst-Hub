package com.batch.label.service;

import com.batch.model.GitHubLabel;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LabelNormalizer {
  private final LabelClassifier classifier;

  public List<GitHubLabel> normalize(List<GitHubLabel> rawLabels) {
    return rawLabels.stream()
        .map(this::convertIfMatched)
        .filter(Objects::nonNull)
        .toList();
  }

  private GitHubLabel convertIfMatched(GitHubLabel raw) {
    return classifier.classify(raw.getName())
        .map(group -> new GitHubLabel(group.getPrimaryName(), group.getColorHex()))
        .orElse(null);
  }
}
