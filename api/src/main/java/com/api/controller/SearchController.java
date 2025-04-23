package com.api.controller;

import com.api.response.SearchResultItem;
import com.api.service.SearchService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {
  private final SearchService searchService;

  @GetMapping("/api/repositories/search")
  public List<SearchResultItem> search(@RequestParam("keyword") String keyword) throws IOException {
    return searchService.searchByKeyword(keyword);
  }
}
