package edu.oakland.LdapGroupSync.v1.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {
  private final Logger accessLog = LoggerFactory.getLogger("access");

  public void logActivity(HttpServletRequest request) {
    String ipAddress = getIpAddress(request);
    logToAccessLog(request, ipAddress);
  }

  public String getIpAddress(HttpServletRequest request) {
    return request.getHeader("X-FORWARDED-FOR") == null
        ? request.getRemoteAddr()
        : request.getHeader("X-FORWARDED-FOR");
  }

  public void logToAccessLog(HttpServletRequest request, String ipAddress) {
    accessLog.info("URL: {} || From IP: {}", request.getServletPath(), ipAddress);
  }
}
