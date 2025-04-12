package com.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.domain.repository")
@EntityScan(basePackages = "com.domain.entity")
@ComponentScan(basePackages = {"com.batch", "com.domain"})
public class BatchApplication {
  public static void main(String[] args) {
    SpringApplication.run(BatchApplication.class, args);
  }
}
