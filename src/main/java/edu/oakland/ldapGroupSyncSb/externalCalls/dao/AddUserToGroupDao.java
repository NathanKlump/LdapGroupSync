package edu.oakland.ldapGroupSyncSb.externalCalls.dao;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AddUserToGroupDao {
  private final LdapApiDao ldapApiDao;

  @Autowired
  public AddUserToGroupDao(LdapApiDao ldapApiDao) {
    this.ldapApiDao = ldapApiDao;
  }

  public void addUserToGroupDao(String groupUid, String netid) {
    ldapApiDao.addGroupMember(groupUid, netid);
  }
}
