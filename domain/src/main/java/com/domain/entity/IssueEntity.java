package com.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "issues")
public class IssueEntity {
  @Id
  private Long id;

  private int issueNumber;

  private String title;

  private String body;

  private String url;

  private String issuer;

  private int commentCount;

  private int reactionCount;

  private LocalDateTime cratedAt;

  private LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "repository_id")
  private GitHubRepositoryEntity gitHubRepositoryEntity;

  @OneToMany(mappedBy = "issue")
  private List<IssueLabelEntity> issueLabelEntities = new ArrayList<>();

}
