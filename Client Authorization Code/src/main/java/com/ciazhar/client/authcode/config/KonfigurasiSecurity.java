package com.ciazhar.client.authcode.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
public class KonfigurasiSecurity extends WebSecurityConfigurerAdapter{

  @Override
  public void configure(WebSecurity web) throws Exception{
    web.ignoring().anyRequest();
  }

}
