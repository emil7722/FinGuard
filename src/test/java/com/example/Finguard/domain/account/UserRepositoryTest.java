package com.example.Finguard.domain.account;

import com.example.Finguard.domain.user.User;
import com.example.Finguard.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @Transactional
    void shouldSaveAndRetrieveUserAndAccount() {
        // 1. Create and save User
        User user = new User();
        user.setEmail("john.doe@finguard.com");
        user.setFullName("John Doe");
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();

        // 2. Create and save Account linked to User
        Account account = new Account();
        account.setUser(savedUser);
        account.setAccountNumber("AZ12FINGUARD00000000000001");
        account.setBalance(new BigDecimal("1500.5000"));
        account.setCurrency("USD");
        Account savedAccount = accountRepository.save(account);

        assertThat(savedAccount.getId()).isNotNull();
        assertThat(savedAccount.getBalance()).isEqualByComparingTo("1500.5000");

        // 3. Verify lookup by email and account number
        var foundUser = userRepository.findByEmail("john.doe@finguard.com");
        assertThat(foundUser).isPresent();

        var foundAccount = accountRepository.findByAccountNumber("AZ12FINGUARD00000000000001");
        assertThat(foundAccount).isPresent();
        assertThat(foundAccount.get().getUser().getFullName()).isEqualTo("John Doe");
    }
}