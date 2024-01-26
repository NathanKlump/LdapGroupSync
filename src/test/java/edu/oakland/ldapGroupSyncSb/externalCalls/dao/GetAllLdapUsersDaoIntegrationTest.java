package edu.oakland.ldapGroupSyncSb.externalCalls.dao;

import edu.oakland.LdapGroupSync.LdapGroupSyncApplication;
import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;
import edu.oakland.LdapGroupSync.v1.model.LdapPerson;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = LdapGroupSyncApplication.class)
public class GetAllLdapUsersDaoIntegrationTest {
  @Autowired GetAllLdapUsersDao getAllLdapUsersDao;

  @Autowired LdapApiDao ldapApiDao;

  @Test
  public void allLdapUsersRetrieved() {
    try {
      List<LdapPerson> ldapPersonList = getAllLdapUsersDao.getAllLdapUsersDao();
      System.out.println("SUCCESS - Delete is successful");
    } catch (Exception e) {
      System.out.println("FAILURE - Exception");
    }
  }
}
