package com.example.securitydemo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecuirtyConfiguration {

  @Order(2)
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();

    http.authorizeHttpRequests()
    .requestMatchers("/profile/**").authenticated()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/management/**").hasAnyRole("ADMIN", "MANAGER")
    .requestMatchers("/*", "/h2-console/**").permitAll(); // h2-console은 보안에 걸려 있다. h2-console/ 로 접근해야 한다.

    // http.httpBasic();
    http.formLogin();

    return http.build();
  }

  @Order(1)
  @Bean
  SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http.securityMatcher("/api/**")
      .authorizeHttpRequests()
      .requestMatchers("/api/public/**").authenticated();


    http.httpBasic();
    return http.build();
  }

  // @Bean
  // public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
  // return new InMemoryUserDetailsManager(
  // User.withUsername("pilseong").password(passwordEncoder().encode("qwe123")).roles("USER").build(),
  // User.withUsername("suel").password(passwordEncoder().encode("qwe123")).roles("USER").build());
  // }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  // @Bean
  // public PasswordEncoder bcryptEncoder() {
  //   return new BCryptPasswordEncoder();
  // }

  @Autowired
  private DataSource dataSource;

  @Bean
  protected JdbcUserDetailsManager userdetailsService() {
    return new JdbcUserDetailsManager(dataSource);
  }
}