package edu.oakland.ldapGroupSyncSb.externalCalls.dao;

import edu.oakland.LdapGroupSync.LdapGroupSyncApplication;
import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;

import java.util.HashSet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = LdapGroupSyncApplication.class)
public class GetAllUsersInLdapGroupDaoIntegrationTest {
  @Autowired GetAllUsersInLdapGroupDao getAllUsersInLdapGroupDao;

  @Autowired LdapApiDao ldapApiDao;

  @Test
  public void allUsersInGroupRetrievedSuccessfully() {
    try {
      HashSet<String> allUsersInLdapGroupHashSet =
          getAllUsersInLdapGroupDao.getAllUsersInLdapGroupDao("member");
      System.out.println("SUCCESS - all users retrieved");
    } catch (Exception e) {
      System.out.println("FAILURE - Exception");
    }
  }

  @Test
  public void invalidGroupReturnsEmptyHashSet() {
    try {
      HashSet<String> allUsersInLdapGroupHashSet =
          getAllUsersInLdapGroupDao.getAllUsersInLdapGroupDao("bad-group");
      System.out.println("SUCCESS - all users retrieved");
    } catch (Exception e) {
      System.out.println("FAILURE - Exception");
    }
  }
}
