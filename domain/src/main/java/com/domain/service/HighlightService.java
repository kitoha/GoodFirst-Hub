package com.domain.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class HighlightService {

  private static final String DEFAULT_PREFIX = "<em>";
  private static final String DEFAULT_SUFFIX = "</em>";
  private static final int DEFAULT_FLAGS = Pattern.CASE_INSENSITIVE;

  public String highlight(String content, String keyword) {
    if (content == null || keyword == null || keyword.isBlank()) {
      return content;
    }

    Pattern pattern = Pattern.compile(Pattern.quote(keyword), DEFAULT_FLAGS);
    Matcher matcher = pattern.matcher(content);

    if(!matcher.find()){
      return null;
    }

    matcher.reset();

    StringBuffer highlighted = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(highlighted, DEFAULT_PREFIX + matcher.group() + DEFAULT_SUFFIX);
    }
    matcher.appendTail(highlighted);

    return highlighted.toString();
  }
}
