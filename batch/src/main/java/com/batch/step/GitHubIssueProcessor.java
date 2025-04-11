package com.batch.step;

import com.batch.model.RepositoryRecord;
import org.springframework.batch.item.ItemProcessor;

public class GitHubIssueProcessor implements ItemProcessor<RepositoryRecord, RepositoryRecord> {

  @Override
  public RepositoryRecord process(RepositoryRecord item) throws Exception {

    if (item.getIssues().isEmpty()) return null;

    return item;
  }
}
