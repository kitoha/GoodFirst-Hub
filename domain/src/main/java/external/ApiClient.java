package external;

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

  public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... pathVariable){
    return exchange(url, HttpMethod.GET, null, responseType, pathVariable);
  }

  public <T>ResponseEntity<T> post(String url,Object body, Class<T> responseType,Object... pathVariable){
    return exchange(url, HttpMethod.POST, body, responseType, pathVariable);
  }

  private <T>ResponseEntity<T> exchange(String url, HttpMethod method, Object body,
      Class<T> responseType, Object... pathVariable){
    HttpEntity<Object> httpEntity = new HttpEntity<>(body, getHeader());
    return restTemplate.exchange(url, method, httpEntity, responseType, pathVariable);
  }

  private HttpHeaders getHeader(){
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

}
