package com.api.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import com.api.response.SearchResultItem;
import com.domain.document.GitHubRepositoryDocument;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class SearchResultHitMapper {

  public Stream<SearchResultItem> mapHit(Hit<GitHubRepositoryDocument> hit) {

    GitHubRepositoryDocument doc = hit.source();
    Map<String, List<String>> hl = hit.highlight();

    // repository 매칭 스니펫
    Stream<SearchResultItem> repoStream = hl.getOrDefault("name", List.of())
        .stream()
        .map(snippet -> toRepositoryItem(doc, snippet));

    // issue 매칭 스니펫
    Stream<SearchResultItem> issueStream = hl.getOrDefault("issueTitles", List.of())
        .stream()
        .map(snippet -> toIssueItem(doc, snippet))
        .flatMap(Optional::stream);

    return Stream.concat(repoStream, issueStream);
  }

  private SearchResultItem toRepositoryItem(
      GitHubRepositoryDocument doc, String snippet) {

    return new SearchResultItem(
        SearchResultItem.ItemType.REPOSITORY,
        doc.getId(),
        doc.getName(),
        snippet,
        null
    );
  }

  private Optional<SearchResultItem> toIssueItem(
      GitHubRepositoryDocument doc, String snippet) {

    String plain = snippet.replaceAll("<em>", "").replaceAll("</em>", "");
    return doc.getIssueTitles().stream()
        .filter(i -> i.getTitle().contains(plain))
        .findFirst()
        .map(issue -> new SearchResultItem(
            SearchResultItem.ItemType.ISSUE,
            doc.getId(),
            doc.getName(),
            snippet,
            issue.getUrl()
        ));
  }
}
