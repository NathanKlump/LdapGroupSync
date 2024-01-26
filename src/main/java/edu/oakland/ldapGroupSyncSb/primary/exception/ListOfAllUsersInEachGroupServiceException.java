package edu.oakland.ldapGroupSyncSb.primary.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ListOfAllUsersInEachGroupServiceException extends Exception {
  public final Exception exception;
  public final ListOfAllUsersInEachGroupServiceExceptionEnum errorCode;

  public enum ListOfAllUsersInEachGroupServiceExceptionEnum {
    NO_USERS_FOUND_IN_MEMBER_GROUP
  };

  public ListOfAllUsersInEachGroupServiceException(
      Exception exception, ListOfAllUsersInEachGroupServiceExceptionEnum errorCode) {
    super("ListOfAllUsersInEachGroupServiceException");
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
