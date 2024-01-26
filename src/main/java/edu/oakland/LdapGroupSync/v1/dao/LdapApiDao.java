package edu.oakland.LdapGroupSync.v1.dao;

import edu.oakland.LdapGroupSync.v1.model.Group;
import edu.oakland.LdapGroupSync.v1.model.LdapPerson;

import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Repository
public class LdapApiDao {
  private WebClient webClient;
  private final String ldapApiBaseUrl;

  private final int THIRTY_MB = 30 * 1024 * 1024;

  @Value("${api.username}")
  private String apiUsername;

  @Value("${api.password}")
  private String apiPassword;

  @Autowired
  public LdapApiDao(
      @Value("${ldap.api.protocol}") String ldapApiProtocol,
      @Value("${ldap.api.hostname}") String ldapApiHostname,
      @Value("${ldap.api.port}") int ldapApiPort) {
    this.ldapApiBaseUrl =
        ldapApiProtocol + "://" + ldapApiHostname + ":" + ldapApiPort + "/LdapApi";
  }

  @PostConstruct
  private void createBaseWebClient() {
    webClient =
        WebClient.builder()
            .baseUrl(ldapApiBaseUrl)
            .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(apiUsername, apiPassword))
            .exchangeStrategies(
                ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(THIRTY_MB))
                    .build())
            .build();
  }

  public List<LdapPerson> getAllAccounts() {
    return webClient
        .get()
        .uri("/v2/ldap-persons")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(LdapPerson.class)
        .collectList()
        .block();
  }

  public HashSet<String> getAllGroupMembers(String groupUid) {
    Group group =
        webClient
            .get()
            .uri(uriBuilder -> uriBuilder.path("/v2/group/{groupuid}").build(groupUid))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(Group.class)
            .block();
    HashSet<String> allMembers = new HashSet<>();
    if (group != null) {
      group.getMemberUids().ifPresent(allMembers::addAll);
      group.getUniqueMembers().ifPresent(allMembers::addAll);
    }
    return allMembers;
  }

  public void addGroupMember(String groupUid, String netid) {
    webClient
        .post()
        .uri(
            uriBuilder ->
                uriBuilder.path("/v2/group/{groupUid}/member/{netId}").build(groupUid, netid))
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  public void removeGroupMember(String groupUid, String netid) {
    webClient
        .delete()
        .uri(
            uriBuilder ->
                uriBuilder.path("/v2/group/{groupUid}/member/{netId}").build(groupUid, netid))
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
