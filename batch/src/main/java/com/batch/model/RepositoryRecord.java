package com.batch.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class RepositoryRecord {
  private String repositoryName;
  private String owner;
  private String address;
  private String language;
  private int starCount;
  @Setter
  List<IssueRecord> issues;
}
