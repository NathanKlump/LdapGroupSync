package edu.oakland.LdapGroupSync.v1.service;

import static edu.oakland.LdapGroupSync.v1.model.GroupType.*;
import static edu.oakland.LdapGroupSync.v1.model.MemberAction.*;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;
import edu.oakland.LdapGroupSync.v1.dao.PostgresApiDao;
import edu.oakland.LdapGroupSync.v1.model.GroupChanges;
import edu.oakland.LdapGroupSync.v1.model.LdapPerson;
import edu.oakland.LdapGroupSync.v1.model.MemberAction;
import edu.oakland.LdapGroupSync.v1.model.StudentVpn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberCompareService {
  private final LdapApiDao ldapApiDao;
  private final PostgresApiDao postgresApiDao;

  private final Predicate<LdapPerson> isFaculty = LdapPerson::hasFacultyAffiliation;
  private final Predicate<LdapPerson> isStaff = LdapPerson::hasStaffAffiliation;
  private final Predicate<LdapPerson> isStudent = LdapPerson::hasStudentAffiliation;
  private final Predicate<LdapPerson> isFacultyOrStaff = isFaculty.or(isStaff);
  private final Predicate<LdapPerson> isGuest = LdapPerson::isGuestAccount;
  private final Predicate<StudentVpn> isExpiredStudentVpn = StudentVpn::isExpired;
  private final Predicate<StudentVpn> shouldBeNotified = StudentVpn::shouldBeNotified;
  private final Predicate<StudentVpn> isActiveStudentVpn = StudentVpn::isActive;

  @Autowired
  public MemberCompareService(LdapApiDao ldapApiDao, PostgresApiDao postgresApiDao) {
    this.ldapApiDao = ldapApiDao;
    this.postgresApiDao = postgresApiDao;
  }

  public GroupChanges getGroupChanges() {
    List<LdapPerson> ldapPeople = ldapApiDao.getAllAccounts();
    List<StudentVpn> studentVpns = postgresApiDao.getAllStudentVpnRecords();

    HashSet<String> expectedFacultyMembers = filterMembers(ldapPeople, isFaculty);
    HashSet<String> expectedStaffMembers = filterMembers(ldapPeople, isStaff);
    HashSet<String> expectedStudentMembers = filterMembers(ldapPeople, isStudent);
    HashSet<String> expectedGuestMembers = filterMembers(ldapPeople, isGuest);
    HashSet<String> expectedVpnMembers = filterMembers(ldapPeople, isFacultyOrStaff);

    HashSet<String> currentFacultyGroupMembers = ldapApiDao.getAllGroupMembers(FACULTY.value);
    HashSet<String> currentStaffGroupMembers = ldapApiDao.getAllGroupMembers(STAFF.value);
    HashSet<String> currentStudentGroupMembers = ldapApiDao.getAllGroupMembers(STUDENT.value);
    HashSet<String> currentGuestGroupMembers = ldapApiDao.getAllGroupMembers(GUEST.value);
    HashSet<String> currentVpnGroupMembers = ldapApiDao.getAllGroupMembers(VPN.value);
    HashSet<String> currentStudentVpnGroupMembers =
        ldapApiDao.getAllGroupMembers(STUDENT_VPN.value);

    GroupChanges changes = new GroupChanges();
    changes.setFaculty(getMemberChanges(currentFacultyGroupMembers, expectedFacultyMembers));
    changes.setStaff(getMemberChanges(currentStaffGroupMembers, expectedStaffMembers));
    changes.setStudent(getMemberChanges(currentStudentGroupMembers, expectedStudentMembers));
    changes.setGuest(getMemberChanges(currentGuestGroupMembers, expectedGuestMembers));
    changes.setVpn(getMemberChanges(currentVpnGroupMembers, expectedVpnMembers));
    changes.setStudentVpn(getStudentVpnChanges(currentStudentVpnGroupMembers, studentVpns));
    return changes;
  }

  public Map<MemberAction, List<String>> getStudentVpnChanges(
      HashSet<String> currentStudentVpnMembers, List<StudentVpn> studentVpns) {
    List<String> expiredMembers =
        studentVpns.stream()
            .filter(isExpiredStudentVpn)
            .map(studentVpn -> studentVpn.id)
            .map(String::valueOf)
            .collect(Collectors.toList());

    List<String> membersToNotify =
        new ArrayList<>(filterStudentVpns(studentVpns, shouldBeNotified));
    HashSet<String> expectedMembers = filterStudentVpns(studentVpns, isActiveStudentVpn);
    Map<MemberAction, List<String>> studentVpnChanges = new HashMap<>();
    Map<MemberAction, List<String>> ldapChanges =
        getMemberChanges(currentStudentVpnMembers, expectedMembers);

    ldapChanges.forEach(studentVpnChanges::put);
    studentVpnChanges.put(EXPIRE, expiredMembers);
    studentVpnChanges.put(NOTIFY, membersToNotify);
    return studentVpnChanges;
  }

  public Map<MemberAction, List<String>> getMemberChanges(
      HashSet<String> currentMembers, HashSet<String> expectedMembers) {
    return Map.of(
        ADD,
        getMemberDifferences(expectedMembers, currentMembers),
        REMOVE,
        getMemberDifferences(currentMembers, expectedMembers));
  }

  private HashSet<String> filterMembers(List<LdapPerson> ldapPeople, Predicate<LdapPerson> filter) {
    return ldapPeople.stream()
        .filter(filter)
        .map(LdapPerson::getUid)
        .filter(Objects::nonNull)
        .collect(Collectors.toCollection(HashSet::new));
  }

  public List<String> getMemberDifferences(
      HashSet<String> membersToFind, HashSet<String> membersToIgnore) {
    return membersToFind.stream()
        .filter(member -> !membersToIgnore.contains(member))
        .collect(Collectors.toList());
  }

  public HashSet<String> filterStudentVpns(
      List<StudentVpn> studentVpns, Predicate<StudentVpn> filter) {
    return studentVpns.stream()
        .filter(filter)
        .map(studentVpn -> studentVpn.netid)
        .filter(Objects::nonNull)
        .collect(Collectors.toCollection(HashSet::new));
  }
}
