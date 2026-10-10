package com.example.Finguard.domain.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    Page<Transaction> findBySourceAccount_IdOrDestinationAccount_Id(UUID sourceAccountId, UUID destinationAccountId, Pageable pageable);

    Page<Transaction> findBySourceAccount_IdOrDestinationAccount_IdAndStatus(UUID sourceAccountId, UUID destinationAccountId, String status, Pageable pageable);
}
