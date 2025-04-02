package com.batch.step;

import com.batch.model.IssueRecord;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class GitHubIssueWriter implements ItemWriter<IssueRecord> {

  @Override
  public void write(Chunk<? extends IssueRecord> chunk) throws Exception {
    for(IssueRecord issueRecord : chunk.getItems()){

    }

  }
}
