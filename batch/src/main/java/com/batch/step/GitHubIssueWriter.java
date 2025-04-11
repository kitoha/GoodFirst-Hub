package com.batch.step;

import com.batch.model.RepositoryRecord;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class GitHubIssueWriter implements ItemWriter<RepositoryRecord> {

  @Override
  public void write(Chunk<? extends RepositoryRecord> chunk) throws Exception {

  }
}
