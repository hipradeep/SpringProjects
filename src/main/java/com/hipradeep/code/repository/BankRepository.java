package com.hipradeep.code.repository;

import com.hipradeep.code.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Bank.
 */
@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> {
    Optional<Bank> findByBankId(Integer bankId);
}
