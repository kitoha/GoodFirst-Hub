package com.batch.step;

import com.batch.model.IssueRecord;
import java.time.LocalDateTime;
import org.springframework.batch.item.ItemProcessor;

public class GitHubIssueProcessor implements ItemProcessor<IssueRecord, IssueRecord> {

  @Override
  public IssueRecord process(IssueRecord item) throws Exception {

    LocalDateTime beforeYear = LocalDateTime.now().minusYears(1);
    if(item.getCreatedAt().isBefore(beforeYear)){
      return null;
    }

    return item;
  }
}
