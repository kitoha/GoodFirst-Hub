package com.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "repositories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepositoryEntity {

  @Id
  private Long id;

  private String name;

  private String owner;

  private String address;

  private int starCount;

  @Builder.Default
  @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL)
  List<IssueEntity> issues = new ArrayList<>();

  public void addIssue(IssueEntity issue) {
    this.issues.add(issue);
    issue.setRepository(this);
  }
}
