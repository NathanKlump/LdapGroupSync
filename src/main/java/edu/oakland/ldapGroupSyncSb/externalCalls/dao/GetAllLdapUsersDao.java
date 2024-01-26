package edu.oakland.ldapGroupSyncSb.externalCalls.dao;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;
import edu.oakland.LdapGroupSync.v1.model.LdapPerson;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GetAllLdapUsersDao {
  private final LdapApiDao ldapApiDao;

  @Autowired
  public GetAllLdapUsersDao(LdapApiDao ldapApiDao) {
    this.ldapApiDao = ldapApiDao;
  }

  public List<LdapPerson> getAllLdapUsersDao() {
    return ldapApiDao.getAllAccounts();
  }
}
