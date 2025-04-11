package com.batch.job;

import com.batch.model.RepositoryRecord;
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
import com.batch.step.GitHubIssueProcessor;
import com.batch.step.GitHubIssueReader;
import com.batch.step.GitHubIssueWriter;

@Configuration
public class BatchConfig {
  @Bean
  public Job goodFirstSearchJob(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      GitHubApiService gitHubApiService){
    return new JobBuilder("goodFirstSearchJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(goodFirstSearchStep(jobRepository, transactionManager, gitHubApiService))
        .build();
  }

  @Bean
  public Step goodFirstSearchStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      GitHubApiService gitHubApiService){
    return new StepBuilder("goodFirstSearchStep", jobRepository)
        .<RepositoryRecord, RepositoryRecord>chunk(10, transactionManager)
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
    return new GitHubIssueProcessor();
  }

  @Bean
  public ItemWriter<RepositoryRecord> githubIssueWriter(){
    return new GitHubIssueWriter();
  }


}
