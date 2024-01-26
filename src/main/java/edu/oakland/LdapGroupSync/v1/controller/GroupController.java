package edu.oakland.LdapGroupSync.v1.controller;

import edu.oakland.LdapGroupSync.v1.model.GroupChanges;
import edu.oakland.LdapGroupSync.v1.service.LoggerService;
import edu.oakland.LdapGroupSync.v1.service.MemberCompareService;
import edu.oakland.LdapGroupSync.v1.service.UpdateService;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/groups")
public class GroupController {
  private final MemberCompareService memberCompareService;
  private final UpdateService updateService;
  private final LoggerService loggerService;

  @Autowired
  public GroupController(
      MemberCompareService memberCompareService,
      UpdateService updateService,
      LoggerService loggerService) {
    this.memberCompareService = memberCompareService;
    this.updateService = updateService;
    this.loggerService = loggerService;
  }

  @GetMapping
  public GroupChanges getGroupChanges(HttpServletRequest request) {
    loggerService.logActivity(request);
    return memberCompareService.getGroupChanges();
  }
  /* ***************************************************************************************** */
  @PostMapping
  public GroupChanges updateGroupMembers(HttpServletRequest request) {
    loggerService.logActivity(request);
    GroupChanges groupChanges = memberCompareService.getGroupChanges();
    updateService.updateAllGroups(groupChanges);
    return groupChanges;
  }
  /* ***************************************************************************************** */
}
