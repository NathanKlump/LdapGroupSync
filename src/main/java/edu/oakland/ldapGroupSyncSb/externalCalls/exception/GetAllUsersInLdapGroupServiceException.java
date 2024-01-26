package edu.oakland.ldapGroupSyncSb.externalCalls.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GetAllUsersInLdapGroupServiceException extends Exception {
  public final Exception exception;
  public final GetAllUsersInLdapGroupServiceEnum errorCode;

  public enum GetAllUsersInLdapGroupServiceEnum {
    NO_USERS_FOUND_IN_GROUP
  };

  public GetAllUsersInLdapGroupServiceException(
      Exception exception, GetAllUsersInLdapGroupServiceEnum errorCode) {
    super("GetAllUsersInLdapGroupServiceException");
    this.exception = exception == null ? new Exception("Generic Exception") : exception;
    this.errorCode = errorCode;
  }

  public String toString() {
    String toString = "Exception: " + saveStackTraceToString(exception) + "\r\n\r\n";
    return toString;
  }

  private String saveStackTraceToString(Exception e) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    return stringWriter.toString();
  }
}
