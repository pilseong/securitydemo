package com.example.securitydemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.securitydemo.entity.Role;
import com.example.securitydemo.repository.RoleRepository;

@SpringBootApplication
public class SecuritydemoApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(SecuritydemoApplication.class, args);
	}


	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) throws Exception {
		Role role = new Role();
		role.setName("ROLE_USER");

		roleRepository.save(role);

		role = new Role();
		role.setName("ROLE_ADMIN");

		roleRepository.save(role);


		role = new Role();
		role.setName("ROLE_MANAGER");

		roleRepository.save(role);
	}

}
