package com.logistics.auth.config;

import com.logistics.auth.entity.User;
import com.logistics.auth.enums.Role;
import com.logistics.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {

		if (userRepository.existsByRole(Role.ADMIN)) {
			return;
		}

		User admin = User.builder().name("System Admin").email("admin@logistics.com")
				.password(passwordEncoder.encode("Admin@123")).role(Role.ADMIN).enabled(true).build();

		userRepository.save(admin);

		System.out.println("========================================");
		System.out.println("Default Admin Created");
		System.out.println("Email    : admin@logistics.com");
		System.out.println("Password : Admin@123");
		System.out.println("========================================");
	}
}