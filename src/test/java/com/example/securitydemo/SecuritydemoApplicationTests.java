package com.example.securitydemo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.securitydemo.entity.User;
import com.example.securitydemo.repository.UserRepository;

@SpringBootTest
class SecuritydemoApplicationTests {

	@Test
	void contextLoads() {
	}

	private final UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	// 의존성 주입
	@Autowired
	public SecuritydemoApplicationTests(UserRepository userRepository) {
			this.userRepository = userRepository;
	}

	@Test
	void signUp() {
			// 멤버 저장
			User user = new User();
			user.setUsername("heops79");
			user.setName("pilseong");
			user.setEmail("heops79@gmail.com");
			user.setPassword(passwordEncoder.encode("qwe123"));
			userRepository.save(user);

			// 저장한 멤버 아이디로 검색
			User findUser = userRepository.findById(user.getId()).get();
			Assertions.assertThat(user.getName()).isEqualTo(findUser.getName());
	}

}
