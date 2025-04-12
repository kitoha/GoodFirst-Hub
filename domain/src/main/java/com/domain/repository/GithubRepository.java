package com.domain.repository;

import com.domain.entity.GitHubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepository extends JpaRepository<GitHubRepositoryEntity, Long> {


}
