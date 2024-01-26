package edu.oakland.ldapGroupSyncSb.primary.service;

import edu.oakland.LdapGroupSync.v1.model.LdapPerson;
import edu.oakland.ldapGroupSyncSb.primary.model.UserGroupsModificationModel;

import org.springframework.stereotype.Service;

@Service
public class UserGroupsModificationService {
  public UserGroupsModificationService() {}

  public UserGroupsModificationModel userGroupsModificationService(LdapPerson ldapPerson) {
    UserGroupsModificationModel userGroupsModificationModel = new UserGroupsModificationModel();
    userGroupsModificationModel.addToMemberGroup = addUserToMemberGroup(ldapPerson);
    userGroupsModificationModel.removeFromMemberGroup = removeUserFromMemberGroup(ldapPerson);
    return userGroupsModificationModel;
  }

  private boolean addUserToMemberGroup(LdapPerson ldapPerson) {
    return false;
  }

  private boolean removeUserFromMemberGroup(LdapPerson ldapPerson) {
    return false;
  }
}
