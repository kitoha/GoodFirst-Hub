package com.api.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.api.response.SearchResultItem;
import com.domain.document.GitHubRepositoryDocument;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
  private final ElasticsearchClient client;
  private final SearchResultHitMapper hitMapper;
  private static final float BOOST_NAME   = 1.0f;
  private static final float BOOST_NGRAM  = 0.8f;
  private static final String INDEX_NAME = "github_repositories";
  private static final Integer SEARCH_COUNT = 5;
  private static final String FIELD_NAME = "name";
  private static final String FIELD_ISSUE = "issueTitles";

  public List<SearchResultItem> searchByKeyword(String keyword) throws IOException {
    Query query = MultiMatchQuery.of(m -> m
        .query(keyword)
        .fields("name^"            + BOOST_NAME,
            "name.ngram^"      + BOOST_NGRAM,
            "issueTitles^"     + BOOST_NAME,
            "issueTitles.ngram^"+ BOOST_NGRAM
        )
        .type(TextQueryType.BestFields)
    )._toQuery();

    SearchRequest req = SearchRequest.of(s -> s
        .index(INDEX_NAME)
        .query(query)
        .size(SEARCH_COUNT)
        .highlight(h -> h
            .fields(FIELD_NAME, field -> field)
            .fields(FIELD_ISSUE, field -> field)
        )
    );

    SearchResponse<GitHubRepositoryDocument> resp =
        client.search(req, GitHubRepositoryDocument.class);

    List<SearchResultItem> result =  resp.hits().hits().stream()
        .flatMap(hitMapper::mapHit)
        .limit(SEARCH_COUNT)
        .toList();

    return result;
  }

}
