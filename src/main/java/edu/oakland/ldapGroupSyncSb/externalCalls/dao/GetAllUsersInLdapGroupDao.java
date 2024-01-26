package edu.oakland.ldapGroupSyncSb.externalCalls.dao;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GetAllUsersInLdapGroupDao {
  private final LdapApiDao ldapApiDao;

  @Autowired
  public GetAllUsersInLdapGroupDao(LdapApiDao ldapApiDao) {
    this.ldapApiDao = ldapApiDao;
  }

  public HashSet<String> getAllUsersInLdapGroupDao(String groupUid) {
    return ldapApiDao.getAllGroupMembers(groupUid);
  }
}
