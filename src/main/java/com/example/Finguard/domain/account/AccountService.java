package com.example.Finguard.domain.account;


import com.example.Finguard.domain.user.User;
import com.example.Finguard.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Account createAccount(AccountRequestDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(request.getAccountNumber());
        account.setBalance(request.getInitialBalance() != null ? request.getInitialBalance() : BigDecimal.ZERO);
        if (request.getCurrency() != null) {
            account.setCurrency(request.getCurrency());
        }

        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByUserId(UUID userId) {
        return accountRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account getAccountById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }
}