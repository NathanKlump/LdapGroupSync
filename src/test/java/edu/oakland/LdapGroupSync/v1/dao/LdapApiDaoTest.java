package edu.oakland.LdapGroupSync.v1.dao;

import static org.junit.jupiter.api.Assertions.*;

import edu.oakland.LdapGroupSync.v1.model.LdapPerson;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LdapApiDaoTest {
  @Autowired private LdapApiDao ldapApiDao;

  @Test
  @Disabled
  void getAllGroupMembers_student_notNull_integrationTest() {
    HashSet<String> students = ldapApiDao.getAllGroupMembers("students");
    assertNotNull(students);
  }

  @Test
  @Disabled
  void getAllAccounts_notNull_integrationTest() {
    List<LdapPerson> people = ldapApiDao.getAllAccounts();
    assertNotNull(people);
  }
}
