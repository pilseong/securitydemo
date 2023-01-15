package com.example.securitydemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/public")
@CrossOrigin
public class PublicRestApiController {

  public PublicRestApiController() {
  }

  @GetMapping("test1")
  public ResponseEntity<Object> test1() {
    return ResponseEntity.ok().body("API Test 1");
  }

  @GetMapping("management/reports")
  public ResponseEntity<Object> test2() {
    return ResponseEntity.ok().body("API for management reports");
  }
}
