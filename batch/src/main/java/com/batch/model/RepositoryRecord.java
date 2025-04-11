package com.batch.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RepositoryRecord {
  private String repositoryName;
  private String owner;
  private String address;
  private int starCount;
  List<IssueRecord> issues = new ArrayList<>();
}
