package edu.oakland.LdapGroupSync.v1.model;

import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class Group {
  private Optional<List<String>> memberUids = Optional.empty();
  private Optional<List<String>> uniqueMembers = Optional.empty();
}
