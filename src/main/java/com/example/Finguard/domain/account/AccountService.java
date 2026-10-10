package com.example.Finguard.domain.account;

import com.example.Finguard.common.exception.NotFoundException;
import com.example.Finguard.common.exception.ValidationException;
import com.example.Finguard.domain.account.dto.AccountSummaryDto;
import com.example.Finguard.domain.auth.UserPrincipal;
import com.example.Finguard.domain.user.User;
import com.example.Finguard.domain.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .orElseThrow(() -> new NotFoundException("User", request.getUserId().toString()));

        if (accountRepository.findByAccountNumber(request.getAccountNumber()).isPresent()) {
            throw new ValidationException("Account number already exists: " + request.getAccountNumber());
        }

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
                .orElseThrow(() -> new NotFoundException("Account", id.toString()));
    }

    @Transactional(readOnly = true)
    public Account getAccountByIdForCurrentUser(UUID id) {
        Account account = getAccountById(id);
        ensureCurrentUserCanAccessAccount(account);
        return account;
    }

    @Transactional(readOnly = true)
    public List<Account> getAccountsByUserIdForCurrentUser(UUID userId) {
        if (isAdmin()) {
            return accountRepository.findByUserId(userId);
        }

        UserPrincipal principal = getCurrentPrincipal();
        if (principal == null || !principal.getUserId().equals(userId)) {
            throw new ValidationException("You are not allowed to view these accounts");
        }

        return accountRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public AccountSummaryDto getAccountSummary(UUID accountId) {
        Account account = getAccountByIdForCurrentUser(accountId);
        return new AccountSummaryDto(
                account.getId(),
                account.getUser().getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus()
        );
    }

    private void ensureCurrentUserCanAccessAccount(Account account) {
        if (isAdmin()) {
            return;
        }

        UserPrincipal principal = getCurrentPrincipal();
        if (principal == null || !principal.getUserId().equals(account.getUser().getId())) {
            throw new ValidationException("You are not allowed to access this account");
        }
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    private UserPrincipal getCurrentPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal;
        }

        return null;
    }
}
