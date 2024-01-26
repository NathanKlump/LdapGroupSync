package edu.oakland.LdapGroupSync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "edu.oakland")
public class LdapGroupSyncApplication {

  public static void main(String[] args) {
    SpringApplication.run(LdapGroupSyncApplication.class, args);
  }
}
