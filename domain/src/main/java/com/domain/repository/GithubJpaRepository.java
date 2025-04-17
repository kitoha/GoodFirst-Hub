package com.domain.repository;

import com.domain.entity.GitHubRepositoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubJpaRepository extends JpaRepository<GitHubRepositoryEntity, Long> {
  Optional<GitHubRepositoryEntity> findByNameAndOwner(String name, String owner);

  List<GitHubRepositoryEntity> findByIndexedFalse();
}
