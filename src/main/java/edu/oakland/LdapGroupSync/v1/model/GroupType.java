package edu.oakland.LdapGroupSync.v1.model;

public enum GroupType {
  FACULTY("faculty"),
  STAFF("staff"),
  STUDENT("students"),
  GUEST("guests"),
  VPN("vpn"),
  STUDENT_VPN("vpnstudents");

  public final String value;

  GroupType(String value) {
    this.value = value;
  }
}
