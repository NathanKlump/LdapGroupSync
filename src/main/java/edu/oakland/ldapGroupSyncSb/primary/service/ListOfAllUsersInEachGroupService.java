package edu.oakland.ldapGroupSyncSb.primary.service;

import edu.oakland.ldapGroupSyncSb.externalCalls.exception.GetAllUsersInLdapGroupServiceException;
import edu.oakland.ldapGroupSyncSb.externalCalls.service.GetAllUsersInLdapGroupService;
import edu.oakland.ldapGroupSyncSb.primary.model.ListOfAllUsersInEachGroupModel;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListOfAllUsersInEachGroupService {
  private final GetAllUsersInLdapGroupService getAllUsersInLdapGroupService;

  @Autowired
  public ListOfAllUsersInEachGroupService(
      GetAllUsersInLdapGroupService getAllUsersInLdapGroupService) {
    this.getAllUsersInLdapGroupService = getAllUsersInLdapGroupService;
  }

  public ListOfAllUsersInEachGroupModel listOfAllUsersInEachGroupService() {
    ListOfAllUsersInEachGroupModel listOfAllUsersInEachGroupModel =
        new ListOfAllUsersInEachGroupModel();
    listOfAllUsersInEachGroupModel.memberGroupUsers = getSetOfAllUsersInMemberGroup();
    return listOfAllUsersInEachGroupModel;
  }

  private HashSet<String> getSetOfAllUsersInMemberGroup() {
    try {
      return getAllUsersInLdapGroupService.getAllUsersInLdapGroupService("member");
    } catch (GetAllUsersInLdapGroupServiceException e) {
      return new HashSet<>();
    }
  }
}
