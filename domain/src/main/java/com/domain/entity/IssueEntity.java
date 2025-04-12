package com.domain.entity;

import jakarta.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "issues")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueEntity {
  @Id
  private Long id;

  private int issueNumber;

  private String title;

  private String url;

  private String issuer;

  private int commentCount;

  private int reactionCount;

  private LocalDateTime cratedAt;

  private LocalDateTime updatedAt;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "repository_id")
  private GitHubRepositoryEntity repository;

  @Builder.Default
  @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
  private List<IssueLabelEntity> issuelabel = new ArrayList<>();

}
