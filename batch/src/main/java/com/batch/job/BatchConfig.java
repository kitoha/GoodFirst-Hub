package com.batch.job;

import com.batch.model.IssueRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
        .<IssueRecord, IssueRecord>chunk(10, transactionManager)
        .reader(githubIssueReader(gitHubApiService))
        .processor(githubIssueProcessor())
        .writer(githubIssueWriter())
        .build();
  }

  @Bean
  public ItemReader<IssueRecord> githubIssueReader(GitHubApiService gitHubApiService){
    return new GitHubIssueReader(gitHubApiService);
  }

  @Bean
  public ItemProcessor<IssueRecord, IssueRecord> githubIssueProcessor(){
    return new GitHubIssueProcessor();
  }

  @Bean
  public ItemWriter<IssueRecord> githubIssueWriter(){
    return new GitHubIssueWriter();
  }


}
