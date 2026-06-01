package com.hipradeep.code.controller;

import com.hipradeep.code.model.HsttBankMst;
import com.hipradeep.code.repository.HsttBankMstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing endpoints for HsttBankMst that employ custom NamedQuery generators.
 */
@RestController
@RequestMapping("/api/banks")
public class BankController {

    private final HsttBankMstRepository bankRepository;

    @Autowired
    public BankController(HsttBankMstRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    /**
     * Creates a new HsttBankMst. The primary key ID is auto-assigned via HQL NamedQuery 'hstt_bank_mst'.
     */
    @PostMapping
    public ResponseEntity<HsttBankMst> createBank(@RequestBody Map<String, Object> payload) {
        Integer hospitalCode = Integer.parseInt(payload.getOrDefault("gnumHospitalCode", "100").toString());
        String bankName = (String) payload.getOrDefault("gstrBankName", "State Bank of Assam");
        String shortName = (String) payload.getOrDefault("gstrBankShortName", "SBA");
        Integer isValid = Integer.parseInt(payload.getOrDefault("gnumIsvalid", "1").toString());

        HsttBankMst bank = new HsttBankMst();
        bank.setGnumHospitalCode(hospitalCode);
        bank.setGstrBankName(bankName);
        bank.setGstrBankShortName(shortName);
        bank.setGnumIsvalid(isValid);

        HsttBankMst savedBank = bankRepository.save(bank);
        return ResponseEntity.ok(savedBank);
    }

    /**
     * Retrieves a single HsttBankMst by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HsttBankMst> getBankById(@PathVariable Integer id) {
        return bankRepository.findByGnumBankId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lists all HsttBankMst currently stored in the database.
     */
    @GetMapping
    public ResponseEntity<List<HsttBankMst>> getAllBanks() {
        return ResponseEntity.ok(bankRepository.findAll());
    }
}
