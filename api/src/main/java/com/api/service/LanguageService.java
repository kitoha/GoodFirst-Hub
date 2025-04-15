package com.api.service;

import com.domain.repository.GithubRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LanguageService {

  private final GithubRepositoryImpl repository;

  public List<String> getLanguages(){
    return repository.findAllLanguage();
  }
}
