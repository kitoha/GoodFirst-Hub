package com.domain.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
  private List<IssueSummary> issueTitles;
}
