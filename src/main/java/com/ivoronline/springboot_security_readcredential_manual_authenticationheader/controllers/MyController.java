package com.ivoronline.springboot_security_readcredential_manual_authenticationheader.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class MyController {

  @Autowired AuthenticationManager authenticationManager;

  //========================================================================
  // AUTHENTICATE
  //========================================================================
  @RequestMapping("/Authenticate")
  public String authenticate(@RequestHeader String authorization) {         //Basic bXl1c2VyOm15dXNlcnBhc3N3b3Jk

    //GET CREDENTIALS
    String   credEncoded = authorization.split(" ")[1];                     //bXl1c2VyOm15dXNlcnBhc3N3b3Jk
    byte[]   credDecoded = Base64.getDecoder().decode(credEncoded);         //[B@7850a50e
    String   credString  = new String(credDecoded, StandardCharsets.UTF_8); //myuser:myuserpassword
    String[] credentials = credString.split(":", 2);                        //[myuser, myuserpassword]
    String   username    = credentials[0];                                  //myuser
    String   password    = credentials[1];                                  //myuserpassword

    //CREATE AUTHENTICATION OBJECT (with Entered Username & Password)
    Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

    //GET    AUTHENTICATION OBJECT (with Authorities)
    authentication = authenticationManager.authenticate(authentication);

    //STORE  AUTHENTICATION OBJECT (into Context)
    SecurityContextHolder.getContext().setAuthentication(authentication);

    //RETURN SOMETHING
    return "User Authenticated";

  }

  //========================================================================
  // HELLO
  //========================================================================
  @Secured("ROLE_USER")
  @RequestMapping("/Hello")
  public String hello() {
    return "Hello from Controller";
  }

}
