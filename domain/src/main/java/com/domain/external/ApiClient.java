package com.domain.external;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ApiClient {

  private final RestTemplate restTemplate;

  public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType,
      Object... pathVariable) {
    return exchange(url, HttpMethod.GET, headers, null, responseType, pathVariable);
  }

  public <T> ResponseEntity<T> post(String url, Object body, HttpHeaders headers,
      Class<T> responseType, Object... pathVariable) {
    return exchange(url, HttpMethod.POST, headers, body, responseType, pathVariable);
  }

  private <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpHeaders headers,
      Object body,
      Class<T> responseType, Object... pathVariable) {
    HttpEntity<Object> httpEntity = new HttpEntity<>(body, headers);
    return restTemplate.exchange(url, method, httpEntity, responseType, pathVariable);
  }

  public HttpHeaders getHeader() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Accept", "application/vnd.github+json");
    headers.set("User-Agent", "GoodFirstHub");
    return headers;
  }

}
