package com.hipradeep.code.repository;

import com.hipradeep.code.model.PrefixedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing new PrefixedEntity records.
 */
@Repository
public interface PrefixedEntityRepository extends JpaRepository<PrefixedEntity, String> {
}
