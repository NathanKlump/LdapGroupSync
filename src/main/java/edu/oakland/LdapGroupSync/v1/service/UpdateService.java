package edu.oakland.LdapGroupSync.v1.service;

import static edu.oakland.LdapGroupSync.v1.model.GroupType.FACULTY;
import static edu.oakland.LdapGroupSync.v1.model.GroupType.GUEST;
import static edu.oakland.LdapGroupSync.v1.model.GroupType.STAFF;
import static edu.oakland.LdapGroupSync.v1.model.GroupType.STUDENT;
import static edu.oakland.LdapGroupSync.v1.model.GroupType.STUDENT_VPN;
import static edu.oakland.LdapGroupSync.v1.model.GroupType.VPN;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.ADD;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.EXPIRE;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.NOTIFY;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.REMOVE;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;
import edu.oakland.LdapGroupSync.v1.dao.PostgresApiDao;
import edu.oakland.LdapGroupSync.v1.model.GroupChanges;
import edu.oakland.LdapGroupSync.v1.model.GroupType;
import edu.oakland.LdapGroupSync.v1.model.MemberAction;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {
  private final LdapApiDao ldapApiDao;
  private final PostgresApiDao postgresApiDao;
  private final EmailService emailService;

  @Autowired
  public UpdateService(
      LdapApiDao ldapApiDao, PostgresApiDao postgresApiDao, EmailService emailService) {
    this.ldapApiDao = ldapApiDao;
    this.postgresApiDao = postgresApiDao;
    this.emailService = emailService;
  }

  public void updateAllGroups(GroupChanges groupChanges) {
    updateSingleGroup(FACULTY, groupChanges.getFaculty());
    updateSingleGroup(STAFF, groupChanges.getStaff());
    updateSingleGroup(STUDENT, groupChanges.getStudent());
    updateSingleGroup(GUEST, groupChanges.getGuest());
    updateSingleGroup(VPN, groupChanges.getVpn());
    updateSingleGroup(STUDENT_VPN, groupChanges.getStudentVpn());

    updateStudentVpnAndNotify(groupChanges.getStudentVpn());
  }

  public void updateSingleGroup(GroupType groupType, Map<MemberAction, List<String>> changes) {
    addMembers(groupType, changes.get(ADD));
    removeMembers(groupType, changes.get(REMOVE));
  }

  public void addMembers(GroupType groupType, List<String> membersToAdd) {
    membersToAdd.forEach(netid -> ldapApiDao.addGroupMember(groupType.value, netid));
  }

  public void removeMembers(GroupType groupType, List<String> membersToRemove) {
    membersToRemove.forEach(netid -> ldapApiDao.removeGroupMember(groupType.value, netid));
  }

  public void updateStudentVpnAndNotify(Map<MemberAction, List<String>> changes) {
    Consumer<String> email = emailService::sendNewStudentVpnAccessEmail;
    Consumer<String> markAsNotified = postgresApiDao::markRecordAsNotified;

    changes.get(EXPIRE).forEach(postgresApiDao::archiveRecord);
    changes.get(NOTIFY).forEach(email.andThen(markAsNotified));
  }
}
