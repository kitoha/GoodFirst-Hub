package com.api.controller;

import com.api.service.LanguageService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/language")
public class LanguageController {

  private LanguageService languageService;

  @GetMapping("")
  public ResponseEntity<List<String>> getLanguageList(){
    return ResponseEntity.ok(languageService.getLanguages());
  }
}
