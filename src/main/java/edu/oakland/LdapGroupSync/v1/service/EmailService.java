package edu.oakland.LdapGroupSync.v1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  public final String fromEmailAddress;
  public final String bccEmailAddress;
  private final JavaMailSenderImpl javaMailSenderImpl;
  private final String oaklandEmailPrefix;
  public static final String NEW_STUDENT_VPN_EMAIL_TEMPLATE =
      "Hello,\n\n"
          + "Your VPN access has been provisioned.  "
          + "As long as you remain an eligible student your access will remain valid until the end of the current semester.  "
          + "Please work with your supervisor prior to end of the current semester if an extension is required.\n\n"
          + "You may connect to the VPN using the directions provided at: https://kb.oakland.edu/uts/GrizzVPN_Instructions_for_Faculty_and_Staff \n\n"
          + "For issues authenticating to the VPN please submit a ticket to University Technology Services (UTS) by emailing uts@oakland.edu.\n\n"
          + "For all other issues please first review the common troubleshooting information available in the Troubleshooting section of the above Knowledge Base (KB).  "
          + "If this information does not resolve your issue please consult with your area’s local Distributed Technology Support (DTS) staff or "
          + "contact the General Helpdesk by emailing helpdesk@oakland.edu";

  @Autowired
  public EmailService(
      @Value("${spring.mail.host}") String springMailHost,
      @Value("${spring.mail.from.email.address}") String fromEmailAddress,
      @Value("${spring.mail.bcc.email.address}") String bccEmailAddress,
      @Value("${oakland.email.prefix}") String oaklandEmailPrefix) {
    this.fromEmailAddress = fromEmailAddress;
    this.bccEmailAddress = bccEmailAddress;
    this.javaMailSenderImpl = new JavaMailSenderImpl();
    this.javaMailSenderImpl.setHost(springMailHost);
    this.oaklandEmailPrefix = oaklandEmailPrefix;
  }

  public void sendNewStudentVpnAccessEmail(String netid) {
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setTo(oaklandEmailPrefix + netid + "@oakland.edu");
    simpleMailMessage.setFrom(fromEmailAddress);
    simpleMailMessage.setBcc(bccEmailAddress);
    simpleMailMessage.setSubject("VPN Access Provisioned");
    simpleMailMessage.setText(NEW_STUDENT_VPN_EMAIL_TEMPLATE);
    javaMailSenderImpl.send(simpleMailMessage);
  }
}
