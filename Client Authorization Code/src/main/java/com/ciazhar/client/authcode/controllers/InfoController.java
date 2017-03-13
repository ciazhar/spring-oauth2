package com.ciazhar.client.authcode.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class InfoController {

  @Autowired
  private OAuth2RestOperations restOperations;

  private String urlApi = "http://localhost:8080/api/halo";

  @RequestMapping("/info")
  public void info(Model m){
    Map<String,String > hasil = restOperations.getForObject(urlApi, HashMap.class);
    m.addAttribute("waktu", new Date().toString());
  }
}
