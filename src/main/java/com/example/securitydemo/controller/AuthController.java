package com.example.securitydemo.controller;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.securitydemo.dtos.LoginDto;
import com.example.securitydemo.dtos.SignUpDto;
import com.example.securitydemo.entity.Role;
import com.example.securitydemo.entity.User;
import com.example.securitydemo.repository.RoleRepository;
import com.example.securitydemo.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private AuthenticationManager authenticationManager;

  private UserRepository userRepository;

  private RoleRepository roleRepository;

  private PasswordEncoder passwordEncoder;

  public AuthController(AuthenticationManager authenticationManager,
    UserRepository userRepository, RoleRepository roleRepository,
    PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/signin")
  public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
    // do the authentication
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      loginDto.getUsernameOrEmail(), loginDto.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      return ResponseEntity.ok("User signed in successfully");
  }

  @PostMapping("/signup")
  public ResponseEntity<String> registerUser(@RequestBody SignUpDto signupDto) {
    // add check for username exists in  the database
    if (userRepository.existsByUsername(signupDto.getUsername())) {
      return new ResponseEntity<>("Username is already taken", HttpStatus.BAD_REQUEST);
    }

    // add check for email already used
    if (userRepository.existsByEmail(signupDto.getEmail())) {
      return new ResponseEntity<>("Email has already taken", HttpStatus.BAD_REQUEST);
    }

    // create user object 
    User user = new User();
    user.setName(signupDto.getName());
    user.setUsername(signupDto.getUsername());
    user.setEmail(signupDto.getEmail());
    user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

    // Collections.singleton returns Set or List wihtout duplication
    Role roles = roleRepository.findByName("ROLE_USER").get();
    user.setRoles(Collections.singleton(roles));
    userRepository.save(user);

    return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
  }
}
