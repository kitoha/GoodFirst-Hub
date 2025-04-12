package com.batch.step;

import com.batch.converter.IssueDtoConverter;
import com.batch.model.GitHubIssueItem;
import com.batch.model.GitHubRepositoryItem;
import com.batch.model.IssueRecord;
import com.batch.model.RepositoryRecord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import com.batch.service.GitHubApiService;


public class GitHubIssueReader implements ItemReader<RepositoryRecord> {

  private int currentPage = 1;
  private static final int MAX_PAGES = 5;
  private final GitHubApiService gitHubApiService;
  private Iterator<RepositoryRecord> repositoryIterator  = Collections.emptyIterator();

  public GitHubIssueReader(GitHubApiService gitHubApiService) {
    this.gitHubApiService = gitHubApiService;
  }

  @Override
  public RepositoryRecord read() throws Exception{
    if(!repositoryIterator.hasNext() && !loadNextPage()){
      return null;
    }
    return repositoryIterator.next();
  }

  private boolean loadNextPage(){
    if(currentPage > MAX_PAGES){
      return false;
    }

    List<GitHubRepositoryItem> repositoryItems = gitHubApiService.fetchHighStarRepo(currentPage++);
    List<RepositoryRecord> repositoryRecords = new ArrayList<>();

    for(GitHubRepositoryItem item : repositoryItems) {
      List<GitHubIssueItem> issueItems = gitHubApiService.fetchIssues(item.getOwner().getLogin(), item.getRepositoryName());
      List<IssueRecord> issueRecords = issueItems.stream().map(IssueDtoConverter::convertToRecord).toList();
      RepositoryRecord repositoryRecord  = RepositoryRecord.builder()
          .repositoryName(item.getRepositoryName())
          .owner(item.getOwner().getLogin())
          .address(item.getHtmlUrl())
          .starCount(item.getStarCount())
          .issues(issueRecords)
          .build();
      repositoryRecords.add(repositoryRecord);
    }

    repositoryIterator = repositoryRecords.iterator();

    return repositoryIterator.hasNext();
  }
}
