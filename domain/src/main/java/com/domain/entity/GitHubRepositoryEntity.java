package com.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "repositories")
public class GitHubRepositoryEntity {

  @Id
  private Long id;

  private String name;

  private String owner;

  private String address;

  private int starCount;

  @OneToMany(mappedBy = "repository", cascade = CascadeType.ALL)
  List<IssueEntity> issues = new ArrayList<>();

}
