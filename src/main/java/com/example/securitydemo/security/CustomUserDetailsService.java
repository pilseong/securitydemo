package com.example.securitydemo.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.securitydemo.entity.Role;
import com.example.securitydemo.entity.User;
import com.example.securitydemo.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    User fetchedUser = userRepository
      .findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
      .orElseThrow(() -> 
        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));

    return new org.springframework.security.core.userdetails.User(
      fetchedUser.getEmail(), 
      fetchedUser.getPassword(), 
      mapRolesToAuthority(fetchedUser.getRoles())
    );
  }

  private Collection<? extends GrantedAuthority> mapRolesToAuthority(Set<Role> roles) {
    return roles.stream().map(role -> 
      new SimpleGrantedAuthority(role.getName())).toList();
  }
}
