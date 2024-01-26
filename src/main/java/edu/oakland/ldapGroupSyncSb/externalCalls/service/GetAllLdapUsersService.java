package edu.oakland.ldapGroupSyncSb.externalCalls.service;

import edu.oakland.LdapGroupSync.v1.model.LdapPerson;
import edu.oakland.ldapGroupSyncSb.externalCalls.dao.GetAllLdapUsersDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllLdapUsersService {
  private final GetAllLdapUsersDao getAllLdapUsersDao;

  @Autowired
  public GetAllLdapUsersService(GetAllLdapUsersDao getAllLdapUsersDao) {
    this.getAllLdapUsersDao = getAllLdapUsersDao;
  }

  public List<LdapPerson> getAllLdapUsersService() {
    return callDaoToGetUsers();
  }

  private List<LdapPerson> callDaoToGetUsers() {
    return getAllLdapUsersDao.getAllLdapUsersDao();
  }
}
