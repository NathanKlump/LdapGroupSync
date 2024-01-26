package edu.oakland.LdapGroupSync.v1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.savedrequest.NullRequestCache;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Value("${web.security.ldap.user.dn.pattern}")
  private String webSecurityLdapUserDnPattern;

  @Value("${web.security.ldap.group.search.base}")
  private String webSecurityLdapGroupSearchBase;

  @Value("${web.security.ldap.manager.dn}")
  private String webSecurityLdapManagerDn;

  @Value("${web.security.ldap.manager.password}")
  private String webSecurityLdapManagerPassword;

  @Value("${web.security.ldap.url}")
  private String webSecurityLdapUrl;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/**")
        .hasAnyRole("IAM_API")
        .anyRequest()
        .fullyAuthenticated()
        .and()
        .requestCache()
        .requestCache(new NullRequestCache())
        .and()
        .httpBasic();
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.ldapAuthentication()
        .userDnPatterns(webSecurityLdapUserDnPattern)
        .groupSearchBase(webSecurityLdapGroupSearchBase)
        .contextSource()
        .managerDn(webSecurityLdapManagerDn)
        .managerPassword(webSecurityLdapManagerPassword)
        .url(webSecurityLdapUrl);
  }
}
