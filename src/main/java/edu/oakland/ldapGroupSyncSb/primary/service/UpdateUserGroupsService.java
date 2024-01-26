package edu.oakland.ldapGroupSyncSb.primary.service;

import edu.oakland.LdapGroupSync.v1.model.LdapPerson;
import edu.oakland.ldapGroupSyncSb.externalCalls.dao.AddUserToGroupDao;
import edu.oakland.ldapGroupSyncSb.externalCalls.dao.RemoveUserFromGroupDao;
import edu.oakland.ldapGroupSyncSb.primary.model.ListOfAllUsersInEachGroupModel;
import edu.oakland.ldapGroupSyncSb.primary.model.UserGroupsModificationModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserGroupsService {
  private final UserGroupsModificationService userGroupsModificationService;
  private final AddUserToGroupDao addUserToGroupDao;
  private final RemoveUserFromGroupDao removeUserFromGroupDao;

  @Autowired
  public UpdateUserGroupsService(
      UserGroupsModificationService userGroupsModificationService,
      AddUserToGroupDao addUserToGroupDao,
      RemoveUserFromGroupDao removeUserFromGroupDao) {
    this.userGroupsModificationService = userGroupsModificationService;
    this.addUserToGroupDao = addUserToGroupDao;
    this.removeUserFromGroupDao = removeUserFromGroupDao;
  }

  public void updateUserGroupsService(
      LdapPerson ldapPerson, ListOfAllUsersInEachGroupModel listOfAllUsersInEachGroupModel) {
    UserGroupsModificationModel userGroupsModificationModel =
        userGroupsModificationService.userGroupsModificationService(ldapPerson);
    addUserToMemberGroup(ldapPerson, userGroupsModificationModel);
    removeUserFromMemberGroup(ldapPerson, userGroupsModificationModel);
  }

  private void addUserToMemberGroup(
      LdapPerson ldapPerson, UserGroupsModificationModel userGroupsModificationModel) {
    if (userGroupsModificationModel.addToMemberGroup) {
      callDaoToAddUserToGroup("groupUid", "netId");
    }
  }

  private void removeUserFromMemberGroup(
      LdapPerson ldapPerson, UserGroupsModificationModel userGroupsModificationModel) {
    if (userGroupsModificationModel.removeFromMemberGroup) {
      callDaoToRemoveUserFromGroup("groupUid", "netId");
    }
  }

  private void callDaoToAddUserToGroup(String groupUid, String netid) {
    addUserToGroupDao.addUserToGroupDao(groupUid, netid);
  }

  private void callDaoToRemoveUserFromGroup(String groupUid, String netid) {
    removeUserFromGroupDao.removeUserFromGroupDao(groupUid, netid);
  }
}
