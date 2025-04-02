package com.batch.step;

import com.batch.model.IssueRecord;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import com.batch.service.GitHubApiService;


public class GitHubIssueReader implements ItemReader<IssueRecord> {

  private int currentPage = 1;
  private static final int MAX_PAGES = 5;
  private GitHubApiService gitHubApiService;
  private Iterator<IssueRecord> currentIterator = Collections.emptyIterator();

  public GitHubIssueReader(GitHubApiService gitHubApiService) {
    this.gitHubApiService = gitHubApiService;
  }

  @Override
  public IssueRecord read() throws Exception{
    if(!hasNextIssue() && !loadNextPage()){
      return null;
    }
    return currentIterator.next();
  }

  private boolean hasNextIssue(){
    return currentIterator != null && currentIterator.hasNext();
  }

  private boolean loadNextPage(){
    if(currentPage > MAX_PAGES){
      return false;
    }

    List<IssueRecord> issueRecords = gitHubApiService.fetchIssues(currentPage);
    if(issueRecords == null || issueRecords.isEmpty()){
      return false;
    }
    currentIterator = issueRecords.iterator();
    currentPage++;
    return true;
  }
}
