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
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class GithubRepositoryImpl implements GithubRepository{

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<GithubRepositoryDto> getRepositories(Pageable pageable) {
    QGitHubRepositoryEntity repo = QGitHubRepositoryEntity.gitHubRepositoryEntity;
    QIssueEntity issue = QIssueEntity.issueEntity;

    List<Tuple> result = queryFactory
        .select(repo, issue.count())
        .from(repo)
        .leftJoin(issue).on(issue.repository.eq(repo))
        .groupBy(repo.id)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(repo.id.desc())
        .fetch();

    //noinspection DataFlowIssue
    List<GithubRepositoryDto> content = result.stream()
        .map(tuple -> GithubRepositoryDto.from(
            tuple.get(repo),
            tuple.get(issue.count())
        ))
        .toList();

    Long total = Optional.ofNullable(queryFactory
            .select(repo.count())
            .from(repo)
            .fetchOne())
        .orElse(0L);

    return new PageImpl<>(content, pageable, total);
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

}
