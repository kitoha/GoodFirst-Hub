package com.domain.repository;

import com.domain.dto.GithubRepositoryDto;
import com.domain.dto.IssueDto;
import com.domain.dto.LabelDto;
import com.domain.entity.IssueEntity;
import com.domain.entity.QGitHubRepositoryEntity;
import com.domain.entity.QIssueEntity;
import com.domain.entity.QIssueLabelEntity;
import com.domain.entity.QLabelEntity;
import com.domain.util.TsidUtil;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class GithubRepositoryImpl implements GithubRepository{

  private final JPAQueryFactory queryFactory;

  @PersistenceContext
  private final EntityManager em;

  @Override
  public Page<GithubRepositoryDto> getRepositories(List<String> languages, List<String> labels, Pageable pageable) {
    QGitHubRepositoryEntity repo = QGitHubRepositoryEntity.gitHubRepositoryEntity;
    QIssueEntity issue = QIssueEntity.issueEntity;
    QIssueLabelEntity issueLabel = QIssueLabelEntity.issueLabelEntity;
    QLabelEntity labelEntity = QLabelEntity.labelEntity;

    JPQLQuery<GithubRepositoryDto> query = queryFactory
        .select(Projections.constructor(
            GithubRepositoryDto.class,
            repo.id,
            repo.name,
            repo.owner,
            repo.primaryLanguage,
            repo.starCount,
            JPAExpressions
                .select(issue.count())
                .from(issue)
                .leftJoin(issue.issuelabel, issueLabel)
                .leftJoin(issueLabel.label, labelEntity)
                .where(
                    issue.repository.id.eq(repo.id),
                    labelIn(labels)
                )
        ))
        .from(repo)
        .where(languageIn(languages))
        .orderBy(repo.starCount.desc());

    long total = query.fetchCount();

    List<GithubRepositoryDto> results = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    return new PageImpl<>(results, pageable, total);
  }

  @Override
  public Page<IssueDto> getIssuesForRepository(String repositoryId, Pageable pageable) {
    QIssueEntity issue = QIssueEntity.issueEntity;
    QLabelEntity label = QLabelEntity.labelEntity;
    QIssueLabelEntity issueLabel = QIssueLabelEntity.issueLabelEntity;
    long repositoryIdLong = TsidUtil.decode(repositoryId);

    List<IssueEntity> issues = queryFactory
        .selectFrom(issue)
        .where(issue.repository.id.eq(repositoryIdLong))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(issue.cratedAt.desc())
        .fetch();

    List<Long> issuedIds = issues.stream()
        .map(IssueEntity::getId)
        .toList();

    List<Tuple> labelTuples = queryFactory
        .select(issueLabel.issue.id, label.name, label.color)
        .from(issueLabel)
        .join(issueLabel.label, label)
        .where(issueLabel.issue.id.in(issuedIds))
        .fetch();

    Map<Long, List<LabelDto>> labelMap = buildLabelMap(labelTuples);

    List<IssueDto> content = toIssueDtoList(issues, labelMap);

    Long total = queryFactory
        .select(issue.count())
        .from(issue)
        .where(issue.repository.id.eq(repositoryIdLong))
        .fetchOne();

    return new PageImpl<>(content, pageable, total != null ? total : 0L);
  }

  @Override
  public List<String> findAllLanguage() {
    QGitHubRepositoryEntity repo = QGitHubRepositoryEntity.gitHubRepositoryEntity;

    return queryFactory
        .select(repo.primaryLanguage)
        .distinct()
        .from(repo)
        .fetch();

  }

  @Override
  public void markAllIndexed(List<Long> repoIds) {
    QGitHubRepositoryEntity repo = QGitHubRepositoryEntity.gitHubRepositoryEntity;

    queryFactory.update(repo)
        .set(repo.indexed, true)
        .where(repo.id.in(repoIds))
        .execute();

    em.flush();
    em.clear();

  }

  private Map<Long, List<LabelDto>> buildLabelMap(List<Tuple> labelTuples) {
    Map<Long, List<LabelDto>> labelMap = new HashMap<>();

    for (Tuple tuple : labelTuples) {
      Long issueId = tuple.get(QIssueLabelEntity.issueLabelEntity.issue.id);
      String name = tuple.get(QLabelEntity.labelEntity.name);
      String color = tuple.get(QLabelEntity.labelEntity.color);

      labelMap
          .computeIfAbsent(issueId, k -> new ArrayList<>())
          .add(new LabelDto(name, color));
    }

    return labelMap;
  }

  private List<IssueDto> toIssueDtoList(List<IssueEntity> issues, Map<Long, List<LabelDto>> labelMap) {
    List<IssueDto> dtos = new ArrayList<>();

    for (IssueEntity issue : issues) {
      dtos.add(new IssueDto(
          TsidUtil.encode(issue.getId()),
          issue.getTitle(),
          issue.getUrl(),
          issue.getIssuer(),
          issue.getCommentCount(),
          issue.getReactionCount(),
          issue.getIssueNumber(),
          issue.getCratedAt(),
          issue.getUpdatedAt(),
          labelMap.getOrDefault(issue.getId(), List.of())
      ));
    }

    return dtos;
  }

  private BooleanExpression languageIn(List<String> languages) {
    return (languages != null && !languages.isEmpty())
        ? QGitHubRepositoryEntity.gitHubRepositoryEntity.primaryLanguage.in(languages)
        : null;
  }

  private BooleanExpression labelIn(List<String> labels) {
    return (labels != null && !labels.isEmpty())
        ? QLabelEntity.labelEntity.name.in(labels)
        : null;
  }

}
