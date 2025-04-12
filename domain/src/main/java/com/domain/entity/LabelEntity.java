package com.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "labels")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String color;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "repository_id")
  private GitHubRepositoryEntity repository;

  @Builder.Default
  @OneToMany(mappedBy = "label", cascade = CascadeType.ALL)
  private List<IssueLabelEntity> issueLabels = new ArrayList<>();

}
