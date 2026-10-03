package com.example.Finguard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class FinGuardApplicationTests {

	// Automatically spins up a real PostgreSQL 16 container for testing
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

	@Test
	void contextLoads() {
		// Verifies that the Spring container boots up successfully
		// and Flyway successfully runs migrations against the test container!
		assertThat(postgres.isRunning()).isTrue();
	}
}