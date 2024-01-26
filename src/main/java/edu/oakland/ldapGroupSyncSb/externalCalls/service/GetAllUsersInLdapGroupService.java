package edu.oakland.ldapGroupSyncSb.externalCalls.service;

import edu.oakland.ldapGroupSyncSb.externalCalls.dao.GetAllUsersInLdapGroupDao;
import edu.oakland.ldapGroupSyncSb.externalCalls.exception.GetAllUsersInLdapGroupServiceException;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllUsersInLdapGroupService {
  private final GetAllUsersInLdapGroupDao getAllUsersInLdapGroupDao;

  @Autowired
  public GetAllUsersInLdapGroupService(GetAllUsersInLdapGroupDao getAllUsersInLdapGroupDao) {
    this.getAllUsersInLdapGroupDao = getAllUsersInLdapGroupDao;
  }

  public HashSet<String> getAllUsersInLdapGroupService(String groupUid)
      throws GetAllUsersInLdapGroupServiceException {
    return callDaoToGetAllUsersInGroup(groupUid);
  }

  private HashSet<String> callDaoToGetAllUsersInGroup(String groupUid)
      throws GetAllUsersInLdapGroupServiceException {
    HashSet<String> allUsersInGroupHashSet =
        getAllUsersInLdapGroupDao.getAllUsersInLdapGroupDao(groupUid);
    if (allUsersInGroupHashSet.isEmpty()) {
      throwExceptionWhenNoUsersFoundInGroup();
    }
    return allUsersInGroupHashSet;
  }

  private void throwExceptionWhenNoUsersFoundInGroup()
      throws GetAllUsersInLdapGroupServiceException {
    throw new GetAllUsersInLdapGroupServiceException(
        null,
        GetAllUsersInLdapGroupServiceException.GetAllUsersInLdapGroupServiceEnum
            .NO_USERS_FOUND_IN_GROUP);
  }
}
