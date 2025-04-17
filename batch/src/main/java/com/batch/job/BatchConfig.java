package com.batch.job;

import com.batch.label.service.LabelNormalizer;
import com.batch.model.RepositoryRecord;
import com.domain.repository.GithubJpaRepository;
import com.domain.repository.IssueJpaRepository;
import com.domain.repository.LabelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.batch.service.GitHubApiService;
import com.batch.step.githubsync.GitHubIssueProcessor;
import com.batch.step.githubsync.GitHubIssueReader;
import com.batch.step.githubsync.GitHubIssueWriter;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final GithubJpaRepository gitHubJpaRepository;
  private final IssueJpaRepository issueJpaRepository;
  private final LabelJpaRepository labelJpaRepository;
  private final LabelNormalizer labelNormalizer;
  private static final String GOOD_FIRST_SEARCH_JOB = "goodFirstSearchJob";
  private static final String GOOD_FIRST_SEARCH_STEP = "goodFirstSearchStep";
  private static final Integer CHUNK_SIZE = 10;
  @Bean
  public Job goodFirstSearchJob(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      GitHubApiService gitHubApiService){
    return new JobBuilder(GOOD_FIRST_SEARCH_JOB, jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(goodFirstSearchStep(jobRepository, transactionManager, gitHubApiService))
        .build();
  }

  @Bean
  public Step goodFirstSearchStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      GitHubApiService gitHubApiService){
    return new StepBuilder(GOOD_FIRST_SEARCH_STEP, jobRepository)
        .<RepositoryRecord, RepositoryRecord>chunk(CHUNK_SIZE, transactionManager)
        .reader(githubIssueReader(gitHubApiService))
        .processor(githubIssueProcessor())
        .writer(githubIssueWriter())
        .build();
  }

  @Bean
  public ItemReader<RepositoryRecord> githubIssueReader(GitHubApiService gitHubApiService){
    return new GitHubIssueReader(gitHubApiService);
  }

  @Bean
  public ItemProcessor<RepositoryRecord, RepositoryRecord> githubIssueProcessor(){
    return new GitHubIssueProcessor(labelNormalizer);
  }

  @Bean
  public ItemWriter<RepositoryRecord> githubIssueWriter(){
    return new GitHubIssueWriter(gitHubJpaRepository, issueJpaRepository, labelJpaRepository);
  }


}
