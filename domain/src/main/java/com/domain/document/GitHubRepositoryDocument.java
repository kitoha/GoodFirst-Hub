package com.domain.document;

import jakarta.persistence.Id;
import java.util.List;
import lombok.Builder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Document(indexName = "github_repositories")
public class GitHubRepositoryDocument {
  @Id
  private String id;

  @Field(type = FieldType.Text)
  private String name;
  @Field(type = FieldType.Keyword)
  private String owner;
  @Field(type = FieldType.Keyword)
  private String address;
  @Field(type = FieldType.Keyword)
  private String primaryLanguage;
  @Field(type = FieldType.Integer)
  private int starCount;
  @Field(type = FieldType.Text)
  private List<String> issueTitles;

}
