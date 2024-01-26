package edu.oakland.ldapGroupSyncSb.externalCalls.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.oakland.ldapGroupSyncSb.externalCalls.dao.GetAllUsersInLdapGroupDao;
import edu.oakland.ldapGroupSyncSb.externalCalls.exception.GetAllUsersInLdapGroupServiceException;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class GetAllUsersInLdapGroupServiceUnitTest {
  @InjectMocks GetAllUsersInLdapGroupService getAllUsersInLdapGroupService;

  @Mock GetAllUsersInLdapGroupDao getAllUsersInLdapGroupDao;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void ldapApiReturnsAllUsersInGroup() {
    HashSet<String> allUsersInLdapGroup = new HashSet<>();
    allUsersInLdapGroup.add("user1");
    allUsersInLdapGroup.add("user2");

    String groupUid = "valid groupUid";

    Mockito.when(getAllUsersInLdapGroupDao.getAllUsersInLdapGroupDao(groupUid))
        .thenReturn(allUsersInLdapGroup);

    try {
      HashSet<String> result =
          getAllUsersInLdapGroupService.getAllUsersInLdapGroupService(groupUid);
      System.out.println("SUCCESS - no exception");
      assertEquals(1, 1);
    } catch (GetAllUsersInLdapGroupServiceException e) {
      System.out.println("FAILURE - exception");
      assertEquals(0, 1);
    }

    System.out.println("test successful");
  }

  @Test
  public void daoReturnsNoUsersInGroupSoServiceThrowsException() {
    GetAllUsersInLdapGroupServiceException getAllUsersInLdapGroupServiceException =
        new GetAllUsersInLdapGroupServiceException(
            null,
            GetAllUsersInLdapGroupServiceException.GetAllUsersInLdapGroupServiceEnum
                .NO_USERS_FOUND_IN_GROUP);

    String groupUid = "invalid groupUid";

    try {
      Mockito.when(getAllUsersInLdapGroupService.getAllUsersInLdapGroupService(groupUid))
          .thenThrow(getAllUsersInLdapGroupServiceException);
    } catch (GetAllUsersInLdapGroupServiceException e) {
    }

    try {
      HashSet<String> result =
          getAllUsersInLdapGroupService.getAllUsersInLdapGroupService(groupUid);
      System.out.println("FAILURE - no exception");
      assertEquals(0, 1);
    } catch (GetAllUsersInLdapGroupServiceException e) {
      System.out.println("SUCCESS - exception");
      assertEquals(1, 1);
    }

    System.out.println("test successful");
  }
}
