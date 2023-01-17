package com.example.securitydemo.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private JwtTokenProvider tokenProvider;

  private CustomUserDetailsService userDetailsService;

  public JwtAuthorizationFilter(JwtTokenProvider tokenProvider,
  CustomUserDetailsService userDetailsService) {
    this.tokenProvider = tokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
          // get JWT from http request
          String authenticationHeader = request.getHeader("Authorization");
    
          // JWT인증이 아닌 경우
          if (!StringUtils.hasText(authenticationHeader)
          || !authenticationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
          }
    
          // validate token
          String token = authenticationHeader.split(" ")[1].trim();
          if (!tokenProvider.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
          }
    
          String username = tokenProvider.getUsernameFromJwt(token);
    
          // load user from database
    
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
          // set spring security
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    
          filterChain.doFilter(request, response);
    
  }
  
}
