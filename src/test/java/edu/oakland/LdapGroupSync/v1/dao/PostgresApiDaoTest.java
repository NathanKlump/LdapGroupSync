package edu.oakland.LdapGroupSync.v1.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostgresApiDaoTest {
  @Autowired private PostgresApiDao postgresApiDao;

  @Test
  @Disabled
  void getAllStudentVpnRecords() {
    assertNotNull(postgresApiDao.getAllStudentVpnRecords());
  }
}
