package com.domain.document;

import jakarta.persistence.Id;
import java.util.List;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Document;

@Builder
@Document(indexName = "github_repositories")
public class GitHubRepositoryDocument {
  @Id
  private String id;

  private String name;

  private String owner;

  private String address;

  private String primaryLanguage;

  private int starCount;

  private List<String> issueTitles;

}
