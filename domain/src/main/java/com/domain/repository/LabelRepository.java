package com.domain.repository;

import com.domain.entity.GitHubRepositoryEntity;
import com.domain.entity.LabelEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends JpaRepository<LabelEntity, Long> {

  Optional<LabelEntity> findByRepositoryAndName(GitHubRepositoryEntity repository, String name);
}
