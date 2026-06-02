package com.hipradeep.code.jpa;

import com.hipradeep.code.model.PrefixedEntity;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

/**
 * Custom Hibernate Identifier Generator generating prefixed and length-constrained UUID strings.
 * Dynamically appends the first three characters of the entity's name into the ID.
 */
public class PrefixedUUIDGenerator implements IdentifierGenerator, Configurable {

    private String prefix;
    private int length;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.prefix = params.getProperty("prefix");
        String lengthVal = params.getProperty("length");
        if (lengthVal != null) {
            try {
                this.length = Integer.parseInt(lengthVal);
            } catch (NumberFormatException e) {
                this.length = 32;
            }
        } else {
            this.length = 32;
        }
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        
        if (uuid.length() > length) {
            uuid = uuid.substring(0, length);
        } else if (uuid.length() < length) {
            StringBuilder sb = new StringBuilder(uuid);
            while (sb.length() < length) {
                sb.append(UUID.randomUUID().toString().replace("-", ""));
            }
            uuid = sb.substring(0, length);
        }

        // Dynamically extract first three characters of the name if entity is PrefixedEntity
        String namePrefix = "";
        if (object instanceof PrefixedEntity) {
            String name = ((PrefixedEntity) object).getName();
            if (name != null) {
                String cleanName = name.replaceAll("[^a-zA-Z0-9]", "");
                if (cleanName.length() > 0) {
                    if (cleanName.length() >= 3) {
                        namePrefix = cleanName.substring(0, 3).toUpperCase() + "_";
                    } else {
                        namePrefix = cleanName.toUpperCase() + "_";
                    }
                }
            }
        }

        StringBuilder finalId = new StringBuilder();
        if (prefix != null) {
            finalId.append(prefix);
        }
        finalId.append(namePrefix);
        finalId.append(uuid);

        return finalId.toString();
    }
}
