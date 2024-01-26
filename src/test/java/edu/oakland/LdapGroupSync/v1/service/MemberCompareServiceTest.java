package edu.oakland.LdapGroupSync.v1.service;

import static edu.oakland.LdapGroupSync.v1.model.MemberAction.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import edu.oakland.LdapGroupSync.v1.dao.LdapApiDao;
import edu.oakland.LdapGroupSync.v1.dao.PostgresApiDao;
import edu.oakland.LdapGroupSync.v1.model.MemberAction;
import edu.oakland.LdapGroupSync.v1.model.StudentVpn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberCompareServiceTest {
  private final LdapApiDao ldapApiDao = mock(LdapApiDao.class);
  private final PostgresApiDao postgresApiDao = mock(PostgresApiDao.class);
  MemberCompareService memberCompareService = new MemberCompareService(ldapApiDao, postgresApiDao);
  private static final HashSet<String> currentMembers = new HashSet<>();
  private static final HashSet<String> expectedMembers = new HashSet<>();
  private static final List<StudentVpn> postgresStudentVpnRecords = new ArrayList<>();
  private static final StudentVpn notifiedNotExpiredNew =
      StudentVpn.builder()
          .id(1)
          .netid("Albert")
          .endDate(LocalDate.now().plusDays(1))
          .infoEmailed(true)
          .build();
  private static final StudentVpn notNotifiedNotExpiredNotNew =
      StudentVpn.builder()
          .id(2)
          .netid("Bob")
          .endDate(LocalDate.now().plusDays(1))
          .infoEmailed(false)
          .build();
  private static final StudentVpn expired =
      StudentVpn.builder().netid("Jane").id(3).endDate(LocalDate.now()).infoEmailed(true).build();
  private static final StudentVpn active =
      StudentVpn.builder()
          .id(4)
          .netid("Chuck")
          .endDate(LocalDate.now().plusDays(1))
          .infoEmailed(true)
          .build();

  @BeforeAll
  static void init() {
    currentMembers.add("Bob");
    currentMembers.add("Jane");
    currentMembers.add("Chuck");

    expectedMembers.add("Bob");
    expectedMembers.add("Jane");
    expectedMembers.add("Sally");

    postgresStudentVpnRecords.add(notifiedNotExpiredNew);
    postgresStudentVpnRecords.add(notNotifiedNotExpiredNotNew);
    postgresStudentVpnRecords.add(expired);
    postgresStudentVpnRecords.add(active);
  }

  @Test
  void getMemberChanges() {
    Map<MemberAction, List<String>> results =
        memberCompareService.getMemberChanges(currentMembers, expectedMembers);
    assertAll(
        () -> assertTrue(results.containsKey(ADD)),
        () -> assertTrue(results.containsKey(REMOVE)),
        () -> assertEquals(1, results.get(REMOVE).size()),
        () -> assertEquals(1, results.get(ADD).size()),
        () -> assertEquals("Chuck", results.get(REMOVE).get(0)),
        () -> assertEquals("Sally", results.get(ADD).get(0)));
  }

  @Test
  void getMemberDifferences_currentMembersVsExpectedMembers_memberInCurrentNotExpected() {
    List<String> members =
        memberCompareService.getMemberDifferences(currentMembers, expectedMembers);
    assertAll(() -> assertEquals(1, members.size()), () -> assertTrue(members.contains("Chuck")));
  }

  @Test
  void getMemberDifferences_expectedMembersVsCurrentMembers_memberInExpectedNotCurrent() {
    List<String> members =
        memberCompareService.getMemberDifferences(expectedMembers, currentMembers);
    assertAll(() -> assertEquals(1, members.size()), () -> assertTrue(members.contains("Sally")));
  }

  @Test
  void getStudentVpnChanges() {
    Map<MemberAction, List<String>> results =
        memberCompareService.getStudentVpnChanges(currentMembers, postgresStudentVpnRecords);
    assertAll(
        () -> assertTrue(results.containsKey(ADD)),
        () -> assertTrue(results.containsKey(REMOVE)),
        () -> assertTrue(results.containsKey(EXPIRE)),
        () -> assertTrue(results.containsKey(NOTIFY)),
        () -> assertEquals(1, results.get(ADD).size()),
        () -> assertEquals(1, results.get(REMOVE).size()),
        () -> assertEquals(1, results.get(EXPIRE).size()),
        () -> assertEquals(1, results.get(NOTIFY).size()),
        () -> assertEquals("Albert", results.get(ADD).get(0)),
        () -> assertEquals("Jane", results.get(REMOVE).get(0)),
        () -> assertEquals("3", results.get(EXPIRE).get(0)),
        () -> assertEquals("Bob", results.get(NOTIFY).get(0)));
  }
}
