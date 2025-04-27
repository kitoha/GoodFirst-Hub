package com.api.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.api.response.SearchResultItem;
import com.domain.document.GitHubRepositoryDocument;
import com.domain.service.HighlightService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final ElasticsearchClient client;
  private final HighlightService highlightService;

  private static final String INDEX_NAME = "github_repositories_v5";
  private static final int SEARCH_COUNT = 20;
  private static final String FIELD_NAME = "name";
  private static final String FIELD_ISSUE = "issueTitles.title";
  private static final double BOOST_NAME = 2.0;
  private static final double BOOST_NGRAM = 1.0;

  public List<SearchResultItem> searchByKeyword(String keyword) throws IOException {

    SearchResponse<GitHubRepositoryDocument> resp = client.search(
        buildRequest(keyword),
        GitHubRepositoryDocument.class
    );

    return resp.hits().hits().stream()
        .map(Hit::source)
        .filter(Objects::nonNull)
        .flatMap(doc -> convertToSearchResult(doc, keyword).stream())
        .toList();
  }

  private SearchRequest buildRequest(String keyword) {
    Query query = MultiMatchQuery.of(m -> m
        .query(keyword)
        .fields(
            FIELD_NAME + "^" + BOOST_NAME,
            FIELD_NAME + ".ngram^" + BOOST_NGRAM,
            FIELD_ISSUE + "^" + BOOST_NAME,
            FIELD_ISSUE + ".ngram^" + BOOST_NGRAM
        )
        .type(TextQueryType.BestFields)
    )._toQuery();

    return SearchRequest.of(s -> s
        .index(INDEX_NAME)
        .query(query)
        .size(SEARCH_COUNT)
        .highlight(h -> h
            .fields(FIELD_NAME, f -> f)
            .fields(FIELD_ISSUE, f -> f)
        )
    );
  }

  private List<SearchResultItem> convertToSearchResult(GitHubRepositoryDocument doc,
      String keyword) {

    List<SearchResultItem> results = new ArrayList<>();
    Optional.ofNullable(mapRepositoryMatch(doc, keyword))
        .ifPresent(results::add);
    results.addAll(mapIssueMatches(doc, keyword));
    return results;
  }

  private SearchResultItem mapRepositoryMatch(GitHubRepositoryDocument doc, String keyword) {
    String name = doc.getName();
    String highlighted = highlightService.highlight(name, keyword);
    if (highlighted == null) {
      return null;
    }

    return new SearchResultItem(
        SearchResultItem.ItemType.REPOSITORY,
        doc.getId(),
        name,
        highlighted,
        null
    );
  }

  private List<SearchResultItem> mapIssueMatches(GitHubRepositoryDocument doc, String keyword) {
    if (doc.getIssueTitles() == null) {
      return List.of();
    }

    return doc.getIssueTitles().stream()
        .map(issue -> {
          String hl = highlightService.highlight(issue.getTitle(), keyword);
          if (hl == null) {
            return null;
          }
          return new SearchResultItem(
              SearchResultItem.ItemType.ISSUE,
              doc.getId(),
              doc.getName(),
              hl,
              issue.getUrl()
          );
        })
        .filter(Objects::nonNull)
        .toList();
  }
}
