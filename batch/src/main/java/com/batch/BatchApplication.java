package com.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.domain.repository")
@EntityScan(basePackages = "com.domain.entity")
@EnableElasticsearchRepositories(basePackages = "com.domain.repository.elasticsearch")
@ComponentScan(basePackages = {"com.batch", "com.domain"})
public class BatchApplication {
  public static void main(String[] args) {
    int exit = SpringApplication.exit(new SpringApplicationBuilder(BatchApplication.class).web(
        WebApplicationType.NONE).run(args));

    System.exit(exit);
  }
}
