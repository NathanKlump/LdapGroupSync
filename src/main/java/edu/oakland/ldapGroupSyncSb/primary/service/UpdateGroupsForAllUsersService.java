package edu.oakland.ldapGroupSyncSb.primary.service;

import edu.oakland.LdapGroupSync.v1.model.LdapPerson;
import edu.oakland.ldapGroupSyncSb.externalCalls.service.GetAllLdapUsersService;
import edu.oakland.ldapGroupSyncSb.primary.model.ListOfAllUsersInEachGroupModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateGroupsForAllUsersService {
  private final GetAllLdapUsersService getAllLdapUsersService;
  private final ListOfAllUsersInEachGroupService listOfAllUsersInEachGroupService;
  private final UpdateUserGroupsService updateUserGroupsService;

  @Autowired
  public UpdateGroupsForAllUsersService(
      GetAllLdapUsersService getAllLdapUsersService,
      ListOfAllUsersInEachGroupService listOfAllUsersInEachGroupService,
      UpdateUserGroupsService updateUserGroupsService) {
    this.getAllLdapUsersService = getAllLdapUsersService;
    this.listOfAllUsersInEachGroupService = listOfAllUsersInEachGroupService;
    this.updateUserGroupsService = updateUserGroupsService;
  }

  public void updateGroupsForAllUsersService() {
    List<LdapPerson> ldapPersonList = getListOfAllLdapUsers();
    ListOfAllUsersInEachGroupModel listOfAllUsersInEachGroupModel = getListOfUsersInEachGroup();
    updateGroupsForAllUsers(ldapPersonList, listOfAllUsersInEachGroupModel);
  }

  private List<LdapPerson> getListOfAllLdapUsers() {
    return getAllLdapUsersService.getAllLdapUsersService();
  }

  private ListOfAllUsersInEachGroupModel getListOfUsersInEachGroup() {
    return listOfAllUsersInEachGroupService.listOfAllUsersInEachGroupService();
  }

  private void updateGroupsForAllUsers(
      List<LdapPerson> ldapPersonList,
      ListOfAllUsersInEachGroupModel listOfAllUsersInEachGroupModel) {
    for (LdapPerson ldapPerson : ldapPersonList) {
      updateUserGroupsService.updateUserGroupsService(ldapPerson, listOfAllUsersInEachGroupModel);
    }
  }
}
