package com.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.domain.repository")
@EntityScan(basePackages = "com.domain.entity")
@ComponentScan(basePackages = {"com.api", "com.domain"})
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class GoodFirstHubApplication {
  public static void main(String[] args) {
    SpringApplication.run(GoodFirstHubApplication.class, args);
  }
}
