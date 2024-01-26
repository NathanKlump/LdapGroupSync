package edu.oakland.LdapGroupSync.v1.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmailServiceTest {
  private final String host = "lsmtp.oakland.edu";
  private final String fromEmail = "noreply@oakland.edu";
  private final String bccEmail = "ea-dev-testing-group@oakland.edu";
  private final String netid = "ea-dev-testing-group";

  @Disabled
  @Test
  void sendNewStudentVpnAccessEmail_newStudentVpnAccess_sendsEmail() {
    // EmailService emailService = new EmailService(host, fromEmail, bccEmail);
    // emailService.sendNewStudentVpnAccessEmail(netid);
  }
}
