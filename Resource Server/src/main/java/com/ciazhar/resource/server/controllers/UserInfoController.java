package com.ciazhar.resource.server.controllers;

import java.security.Principal;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableResourceServer
public class UserInfoController{
    @RequestMapping("/userInfo")
    public Principal userInfo(Principal principal){
      return principal;
    }
}
