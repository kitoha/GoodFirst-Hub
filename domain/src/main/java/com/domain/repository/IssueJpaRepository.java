package com.domain.repository;

import com.domain.entity.GitHubRepositoryEntity;
import com.domain.entity.IssueEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueJpaRepository extends JpaRepository<IssueEntity, Long> {
  Optional<IssueEntity> findByRepositoryAndUrl(GitHubRepositoryEntity repository, String url);

  List<IssueEntity> findByRepositoryIdIn(List<Long> repoIds);
}
