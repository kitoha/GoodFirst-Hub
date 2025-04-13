package com.batch.label.service;

import com.batch.label.enums.LabelGroup;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LabelClassifier {

  public Optional<LabelGroup> classify(String rawLabel) {
    if (rawLabel == null) return Optional.empty();

    return Arrays.stream(LabelGroup.values())
        .filter(group -> group.matches(rawLabel))
        .findFirst();
  }
}
