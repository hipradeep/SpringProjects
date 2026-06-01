package com.hipradeep.code.repository;

import com.hipradeep.code.model.HsttBankMst;
import com.hipradeep.code.model.HsttBankMstId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for HsttBankMst.
 */
@Repository
public interface HsttBankMstRepository extends JpaRepository<HsttBankMst, HsttBankMstId> {
    Optional<HsttBankMst> findByGnumBankId(Integer gnumBankId);
}
