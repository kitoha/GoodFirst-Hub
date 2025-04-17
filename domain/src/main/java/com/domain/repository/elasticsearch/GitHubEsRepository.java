package com.domain.repository.elasticsearch;

import com.domain.document.GitHubRepositoryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GitHubEsRepository extends ElasticsearchRepository<GitHubRepositoryDocument, String> {

}
