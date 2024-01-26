package edu.oakland.LdapGroupSync.v1.model;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class GroupChanges {
  private Map<MemberAction, List<String>> faculty;
  private Map<MemberAction, List<String>> staff;
  private Map<MemberAction, List<String>> student;
  private Map<MemberAction, List<String>> guest;
  private Map<MemberAction, List<String>> vpn;
  private Map<MemberAction, List<String>> studentVpn;
}
