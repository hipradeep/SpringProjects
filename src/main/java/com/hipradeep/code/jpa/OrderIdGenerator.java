package com.hipradeep.code.jpa;

import com.hipradeep.code.model.Order;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

/**
 * Custom Hibernate Identifier Generator generating a prefixed ID:
 * - Prefix configured dynamically from the Entity's @GenericGenerator annotations.
 * - Last 4 digits of the itemId.
 * - 2-digit sequence incrementing from 00 to 99 for that specific item.
 */
public class OrderIdGenerator implements IdentifierGenerator, Configurable {

    private String prefix;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.prefix = params.getProperty("prefix");
        if (this.prefix == null) {
            this.prefix = "ODER_"; // Fallback default
        }
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Long itemId = null;
        if (object instanceof Order) {
            itemId = ((Order) object).getItemId();
        }

        String itemIdStr = itemId != null ? itemId.toString() : "0000";
        // Keep only digits
        String cleanItemId = itemIdStr.replaceAll("[^0-9]", "");
        String last4;
        if (cleanItemId.length() >= 4) {
            last4 = cleanItemId.substring(cleanItemId.length() - 4);
        } else {
            int val = 0;
            if (!cleanItemId.isEmpty()) {
                try {
                    val = Integer.parseInt(cleanItemId);
                } catch (NumberFormatException ignored) {}
            }
            last4 = String.format("%04d", val);
        }

        // Query database to find the maximum existing order ID for this itemId with same base prefix
        String maxId = session.createQuery(
                "select max(o.id) from Order o where o.itemId = :itemId and o.id like :pattern", String.class)
                .setParameter("itemId", itemId)
                .setParameter("pattern", prefix + last4 + "%")
                .uniqueResult();

        int maxSeq = -1;
        if (maxId != null && maxId.startsWith(prefix) && maxId.length() == prefix.length() + 6) {
            try {
                maxSeq = Integer.parseInt(maxId.substring(prefix.length() + 4));
            } catch (NumberFormatException ignored) {}
        }

        int nextSeq = (maxSeq + 1) % 100;
        return prefix + last4 + String.format("%02d", nextSeq);
    }
}
