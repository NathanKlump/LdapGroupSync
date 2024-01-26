package edu.oakland.ldapGroupSyncSb.externalCalls.dao;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RemoveUserFromGroupDao {
  private final LdapApiDao ldapApiDao;

  @Autowired
  public RemoveUserFromGroupDao(LdapApiDao ldapApiDao) {
    this.ldapApiDao = ldapApiDao;
  }

  public void removeUserFromGroupDao(String groupUid, String netid) {
    ldapApiDao.removeGroupMember(groupUid, netid);
  }
}
