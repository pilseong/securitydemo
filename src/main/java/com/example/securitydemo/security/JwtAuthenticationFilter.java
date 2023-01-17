package com.example.securitydemo.security;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.securitydemo.dtos.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private PasswordEncoder passwordEncoder;


  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    Authentication authentication = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(username, password));
      // .authenticate(new UsernamePasswordAuthenticationToken(username, passwordEncoder.encode(password)));
    
    return authentication;
    // SecurityContextHolder.getContext().setAuthentication(authentication);
    //       return ResponseEntity.ok("User signed in successfully");
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {
    
    // UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

    String token = jwtTokenProvider.generateToken(authResult);

    response.addHeader("Authorization", "Bearer " +  token);
      
  }
  
}
