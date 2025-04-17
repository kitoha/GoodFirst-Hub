package com.batch.step.indexing;

import com.domain.entity.GitHubRepositoryEntity;
import com.domain.repository.GithubJpaRepository;
import java.util.Iterator;
import org.springframework.batch.item.ItemReader;

public class UnindexedRepositoryReader implements ItemReader<GitHubRepositoryEntity> {
  
  private final GithubJpaRepository repository;
  private Iterator<GitHubRepositoryEntity> iterator;

  public UnindexedRepositoryReader(GithubJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public GitHubRepositoryEntity read() throws Exception {

    if(iterator == null) {
      iterator = repository.findByIndexedFalse().iterator();
    }

    return iterator.hasNext() ? iterator.next() : null;
  }
}
