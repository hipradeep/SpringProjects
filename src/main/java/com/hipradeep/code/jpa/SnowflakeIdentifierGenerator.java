package com.hipradeep.code.jpa;

import com.hipradeep.code.util.SnowflakeIdGenerator;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * Hibernate custom identifier generator returning 64-bit Snowflake IDs.
 */
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {

    private static final SnowflakeIdGenerator GENERATOR = new SnowflakeIdGenerator(1, 1);

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return GENERATOR.nextId();
    }
}
