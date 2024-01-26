package edu.oakland.ldapGroupSyncSb.externalCalls.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.oakland.LdapGroupSync.v1.model.LdapPerson;
import edu.oakland.ldapGroupSyncSb.externalCalls.dao.GetAllLdapUsersDao;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GetAllLdapUsersServiceUnitTest {
  @InjectMocks GetAllLdapUsersService getAllLdapUsersService;

  @Mock GetAllLdapUsersDao getAllLdapUsersDao;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void ldapApiReturnsAllUserRecords() {
    List<LdapPerson> ldapPersonList = new LinkedList<>();

    List<String> eduPersonAffiliation = new LinkedList<>();
    List<String> ouEduPersonEmploymentOrg = new LinkedList<>();

    LdapPerson ldapPerson =
        new LdapPerson(
            "uid",
            "ouEduPersonUUID",
            "eduPersonPrimaryAffiliation",
            eduPersonAffiliation,
            "shadowExpire",
            "ouEduPersonLastTermReg",
            "ouNonPersonAccountOwner",
            "ouNonPersonAffiliation",
            "ouGuestAccountSponsor",
            "ouEduPersonExpire",
            ouEduPersonEmploymentOrg);

    ldapPersonList.add(ldapPerson);

    Mockito.when(getAllLdapUsersDao.getAllLdapUsersDao()).thenReturn(ldapPersonList);

    List<LdapPerson> result = getAllLdapUsersService.getAllLdapUsersService();
    assertEquals(1, result.size());

    System.out.println("test successful");
  }
}
