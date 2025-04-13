package com.batch.label.enums;

import java.util.List;
import lombok.Getter;


@Getter
public enum LabelGroup {
  GOOD_FIRST_ISSUE(
      "good first issue",
      List.of("good first issue", "beginner", "easy", "first-timers-only"),
      "#7057ff"
  ),

  HELP_WANTED(
      "help wanted",
      List.of("help wanted", "assistance", "support-needed"),
      "#008672"
  ),

  BUG(
      "bug",
      List.of("bug", "type: bug", "confirmed bug"),
      "#d73a4a"
  ),

  ENHANCEMENT(
      "enhancement",
      List.of("enhancement", "feature", "improvement"),
      "#a2eeef"
  ),

  QUESTION(
      "question",
      List.of("question", "inquiry", "clarification"),
      "#d876e3"
  ),

  DOCUMENTATION(
      "documentation",
      List.of("documentation", "docs", "doc-fix"),
      "#0075ca"
  ),

  DISCUSSION(
      "discussion",
      List.of("discussion", "rfc", "proposal", "design-decision"),
      "#cfd3d7"
  ),

  INVALID(
      "invalid",
      List.of("invalid", "not a bug", "misuse"),
      "#e4e669"
  ),

  WONTFIX(
      "wontfix",
      List.of("wontfix", "decline"),
      "#ffffff"
  ),

  DUPLICATE(
      "duplicate",
      List.of("duplicate", "repeated"),
      "#cfd3d7"
  );

  private final String primaryName;
  private final List<String> keywords;
  private final String colorHex;

  LabelGroup(String primaryName, List<String> keywords, String colorHex) {
    this.primaryName = primaryName;
    this.keywords = keywords;
    this.colorHex = colorHex;
  }

  public boolean matches(String label) {
    if (label == null) {
      return false;
    }
    String normalized = label.trim().toLowerCase();
    return keywords.stream().anyMatch(normalized::contains);
  }
}
