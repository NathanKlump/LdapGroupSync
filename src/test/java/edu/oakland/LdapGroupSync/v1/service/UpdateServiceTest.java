package edu.oakland.LdapGroupSync.v1.service;

import static edu.oakland.LdapGroupSync.v1.model.GroupType.STUDENT;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.ADD;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.EXPIRE;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.NOTIFY;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.REMOVE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;
import edu.oakland.LdapGroupSync.v1.dao.PostgresApiDao;
import edu.oakland.LdapGroupSync.v1.model.GroupChanges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UpdateServiceTest {
  private final LdapApiDao ldapApiDao = mock(LdapApiDao.class);
  private final PostgresApiDao postgresApiDao = mock(PostgresApiDao.class);
  private final EmailService emailService = mock(EmailService.class);
  private UpdateService updateService;
  private GroupChanges groupChanges;
  private final String fakeNetid1 = "testaccount9000";
  private final String fakeNetid2 = "testaccount9001";
  private final String fakeNetid3 = "testaccount9002";
  private final List<String> listOf1FakeNetid = List.of(fakeNetid1);
  private final List<String> listOf3FakeNetids = List.of(fakeNetid1, fakeNetid2, fakeNetid3);

  @BeforeEach
  public void init() {
    updateService = new UpdateService(ldapApiDao, postgresApiDao, emailService);
    groupChanges = new GroupChanges();
    groupChanges.setStudentVpn(new HashMap<>());
    groupChanges.getStudentVpn().put(ADD, new ArrayList<>());
    groupChanges.getStudentVpn().put(REMOVE, new ArrayList<>());
    groupChanges.getStudentVpn().put(NOTIFY, new ArrayList<>());
    groupChanges.getStudentVpn().put(EXPIRE, new ArrayList<>());
  }

  @Test
  void addMembers_emptyList_neverAccessed() {
    updateService.addMembers(STUDENT, new ArrayList<>());
    verify(ldapApiDao, never()).addGroupMember(any(), anyString());
  }

  @Test
  void addMembers_oneNetids_accessedOnce() {
    updateService.addMembers(STUDENT, listOf1FakeNetid);
    verify(ldapApiDao, times(1)).addGroupMember(any(), anyString());
  }

  @Test
  void addMembers_threeNetid_accessedThreeTimes() {
    updateService.addMembers(STUDENT, listOf3FakeNetids);
    verify(ldapApiDao, times(3)).addGroupMember(any(), anyString());
  }

  @Test
  void removeMembers_emptyList_neverAccessed() {
    updateService.removeMembers(STUDENT, new ArrayList<>());
    verify(ldapApiDao, never()).removeGroupMember(any(), anyString());
  }

  @Test
  void removeMembers_oneNetids_accessedOnce() {
    updateService.removeMembers(STUDENT, listOf1FakeNetid);
    verify(ldapApiDao, times(1)).removeGroupMember(any(), anyString());
  }

  @Test
  void removeMembers_threeNetid_accessedThreeTimes() {
    updateService.removeMembers(STUDENT, listOf3FakeNetids);
    verify(ldapApiDao, times(3)).removeGroupMember(any(), anyString());
  }

  @Test
  void updateStudentVpnAndNotify_emptyLists_neverAccessed() {
    updateService.updateStudentVpnAndNotify(groupChanges.getStudentVpn());

    verify(postgresApiDao, never()).archiveRecord(anyString());
    verify(ldapApiDao, never()).removeGroupMember(any(), anyString());
    verify(emailService, never()).sendNewStudentVpnAccessEmail(anyString());
    verify(postgresApiDao, never()).markRecordAsNotified(anyString());
  }

  @Test
  void updateStudentVpnAndNotify_singleExpire_expiresAndRemoveFromGroup() {
    groupChanges.getStudentVpn().get(EXPIRE).add(fakeNetid1);
    updateService.updateStudentVpnAndNotify(groupChanges.getStudentVpn());

    verify(postgresApiDao, times(1)).archiveRecord(anyString());
    verify(emailService, never()).sendNewStudentVpnAccessEmail(anyString());
    verify(postgresApiDao, never()).markRecordAsNotified(anyString());
  }

  @Test
  void updateStudentVpnAndNotify_threeExpires_expiresAndRemovesAllThree() {
    groupChanges.getStudentVpn().get(EXPIRE).addAll(listOf3FakeNetids);
    updateService.updateStudentVpnAndNotify(groupChanges.getStudentVpn());

    verify(postgresApiDao, times(3)).archiveRecord(anyString());
    verify(emailService, never()).sendNewStudentVpnAccessEmail(anyString());
    verify(postgresApiDao, never()).markRecordAsNotified(anyString());
  }

  @Test
  void updateStudentVpnAndNotify_singleNotify_notifiesAndMarksNotified() {
    groupChanges.getStudentVpn().get(NOTIFY).add(fakeNetid1);
    updateService.updateStudentVpnAndNotify(groupChanges.getStudentVpn());

    verify(postgresApiDao, never()).archiveRecord(anyString());
    verify(ldapApiDao, never()).removeGroupMember(any(), anyString());
    verify(emailService, times(1)).sendNewStudentVpnAccessEmail(anyString());
    verify(postgresApiDao, times(1)).markRecordAsNotified(anyString());
  }

  @Test
  void updateStudentVpnAndNotify_threeNotify_notifiesAndMarksNotifiedForAllThree() {
    groupChanges.getStudentVpn().get(NOTIFY).addAll(listOf3FakeNetids);
    updateService.updateStudentVpnAndNotify(groupChanges.getStudentVpn());

    verify(postgresApiDao, never()).archiveRecord(anyString());
    verify(ldapApiDao, never()).removeGroupMember(any(), anyString());
    verify(emailService, times(3)).sendNewStudentVpnAccessEmail(anyString());
    verify(postgresApiDao, times(3)).markRecordAsNotified(anyString());
  }

  @Test
  void updateStudentVpnAndNotify_threeNotifyAndThreeExpire_notifiesAndMarksNotifiedForAllThree() {
    groupChanges.getStudentVpn().get(EXPIRE).addAll(listOf3FakeNetids);
    groupChanges.getStudentVpn().get(NOTIFY).addAll(listOf3FakeNetids);
    updateService.updateStudentVpnAndNotify(groupChanges.getStudentVpn());

    verify(postgresApiDao, times(3)).archiveRecord(anyString());
    verify(emailService, times(3)).sendNewStudentVpnAccessEmail(anyString());
    verify(postgresApiDao, times(3)).markRecordAsNotified(anyString());
  }
}
