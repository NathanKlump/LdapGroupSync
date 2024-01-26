package edu.oakland.LdapGroupSync.v1.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@Jacksonized
@ToString
public class LdapPerson {
  public final String uid;
  public final String ouEduPersonUUID;
  public final String eduPersonPrimaryAffiliation;
  public final List<String> eduPersonAffiliation;
  public final String shadowExpire;
  public final String ouEduPersonLastTermReg;
  public final String ouNonPersonAccountOwner;
  public final String ouNonPersonAffiliation;
  public final String ouGuestAccountSponsor;
  public final String ouEduPersonExpire;
  public final List<String> ouEduPersonEmploymentOrg;

  public LdapPerson(
      String uid,
      String ouEduPersonUUID,
      String eduPersonPrimaryAffiliation,
      List<String> eduPersonAffiliation,
      String shadowExpire,
      String ouEduPersonLastTermReg,
      String ouNonPersonAccountOwner,
      String ouNonPersonAffiliation,
      String ouGuestAccountSponsor,
      String ouEduPersonExpire,
      List<String> ouEduPersonEmploymentOrg) {
    this.uid = uid;
    this.ouEduPersonUUID = ouEduPersonUUID;
    this.eduPersonPrimaryAffiliation = eduPersonPrimaryAffiliation;
    this.eduPersonAffiliation = eduPersonAffiliation;
    this.shadowExpire = shadowExpire;
    this.ouEduPersonLastTermReg = ouEduPersonLastTermReg;
    this.ouNonPersonAccountOwner = ouNonPersonAccountOwner;
    this.ouNonPersonAffiliation = ouNonPersonAffiliation;
    this.ouGuestAccountSponsor = ouGuestAccountSponsor;
    this.ouEduPersonExpire = ouEduPersonExpire;
    this.ouEduPersonEmploymentOrg = ouEduPersonEmploymentOrg;
  }

  public boolean hasFacultyAffiliation() {
    return hasPrimaryFacultyAffiliation() || hasSecondaryFacultyAffiliation();
  }

  public boolean hasStaffAffiliation() {
    return hasPrimaryStaffAffiliation() || hasSecondaryStaffAffiliation();
  }

  public boolean hasStudentAffiliation() {
    return hasPrimaryStudentAffiliation() || hasSecondaryStudentAffiliation();
  }

  public boolean hasPrimaryFacultyAffiliation() {
    return eduPersonPrimaryAffiliation != null
        && eduPersonPrimaryAffiliation.equalsIgnoreCase("faculty");
  }

  public boolean hasPrimaryStaffAffiliation() {
    return eduPersonPrimaryAffiliation != null
        && eduPersonPrimaryAffiliation.equalsIgnoreCase("staff");
  }

  public boolean hasPrimaryStudentAffiliation() {
    return eduPersonPrimaryAffiliation != null
        && eduPersonPrimaryAffiliation.equalsIgnoreCase("student");
  }

  public boolean hasSecondaryFacultyAffiliation() {
    return eduPersonAffiliation != null && eduPersonAffiliation.contains("faculty");
  }

  public boolean hasSecondaryStaffAffiliation() {
    return eduPersonAffiliation != null && eduPersonAffiliation.contains("staff");
  }

  public boolean hasSecondaryStudentAffiliation() {
    return eduPersonAffiliation != null && eduPersonAffiliation.contains("student");
  }

  public boolean isGuestAccount() {
    return eduPersonAffiliation != null && eduPersonAffiliation.contains("affiliate");
  }
}
