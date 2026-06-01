package com.hipradeep.code.controller;

import com.hipradeep.code.model.Bank;
import com.hipradeep.code.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing endpoints for Bank that employ custom NamedQuery generators.
 */
@RestController
@RequestMapping("/api/banks")
public class BankController {

    private final BankRepository bankRepository;

    @Autowired
    public BankController(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    /**
     * Creates a new Bank. The primary key ID is auto-assigned via HQL NamedQuery 'bank'.
     */
    @PostMapping
    public ResponseEntity<Bank> createBank(@RequestBody Map<String, Object> payload) {
        String bankName = (String) payload.getOrDefault("bankName", "State Bank of Assam");
        String shortName = (String) payload.getOrDefault("bankShortName", "SBA");

        Bank bank = new Bank();
        bank.setBankName(bankName);
        bank.setBankShortName(shortName);

        Bank savedBank = bankRepository.save(bank);
        return ResponseEntity.ok(savedBank);
    }

    /**
     * Retrieves a single Bank by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(@PathVariable Integer id) {
        return bankRepository.findByBankId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists all Bank currently stored in the database.
     */
    @GetMapping
    public ResponseEntity<List<Bank>> getAllBanks() {
        return ResponseEntity.ok(bankRepository.findAll());
    }
}
