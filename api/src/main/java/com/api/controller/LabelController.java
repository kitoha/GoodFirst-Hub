package com.api.controller;

import com.api.service.LabelService;
import com.domain.dto.LabelDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

  private final LabelService labelService;

  @GetMapping("")
  public ResponseEntity<List<LabelDto>> getLabels(Pageable pageable){
    return ResponseEntity.ok(labelService.getLabels());
  }

}
