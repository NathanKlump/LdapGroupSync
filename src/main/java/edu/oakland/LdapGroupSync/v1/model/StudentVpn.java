package edu.oakland.LdapGroupSync.v1.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class StudentVpn {
  public int id;
  public String netid;
  public String firstName;
  public String lastName;
  public String studentLevel;
  public LocalDate dateAdded;
  public LocalDate dateStarted;
  public LocalDate endDate;
  public LocalDate dateRemoved;
  public boolean infoEmailed;

  public boolean isExpired() {
    LocalDate now = LocalDate.now();
    return this.endDate != null && (this.endDate.isBefore(now) || endDate.isEqual(now));
  }

  public boolean isActive() {
    return this.endDate != null && this.endDate.isAfter(LocalDate.now());
  }

  public boolean shouldBeNotified() {
    return !infoEmailed;
  }
}
