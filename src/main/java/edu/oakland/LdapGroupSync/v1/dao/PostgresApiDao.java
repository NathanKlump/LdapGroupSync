package edu.oakland.LdapGroupSync.v1.dao;

import edu.oakland.LdapGroupSync.v1.model.StudentVpn;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
public class PostgresApiDao {
  private WebClient webClient;
  private final String postgresApiBaseUrl;

  @Value("${api.username}")
  private String apiUsername;

  @Value("${api.password}")
  private String apiPassword;

  @Autowired
  public PostgresApiDao(
      @Value("${postgres.api.protocol}") String postgresApiProtocol,
      @Value("${postgres.api.hostname}") String postgresApiHostname,
      @Value("${postgres.api.port}") int postgresApiPort) {
    this.postgresApiBaseUrl =
        postgresApiProtocol + "://" + postgresApiHostname + ":" + postgresApiPort + "/PostgresApi";
  }

  @PostConstruct
  private void createBaseWebClient() {
    webClient =
        WebClient.builder()
            .baseUrl(postgresApiBaseUrl)
            .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(apiUsername, apiPassword))
            .build();
  }

  public List<StudentVpn> getAllStudentVpnRecords() {
    return webClient
        .get()
        .uri("/v2/student-vpn")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(StudentVpn.class)
        .collectList()
        .block();
  }

  public void markRecordAsNotified(String netid) {
    webClient
        .put()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/v2/student-vpn/{netid}/info-emailed/{wasNotified}")
                    .build(netid, true))
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  public void archiveRecord(String id) {
    webClient
        .post()
        .uri(uriBuilder -> uriBuilder.path("/v2/student-vpn/archive/{id}").build(id))
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
