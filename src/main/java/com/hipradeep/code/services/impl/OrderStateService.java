package com.hipradeep.code.services.impl;

import com.hipradeep.code.entities.Order;
import com.hipradeep.code.entities.Order2;
import com.hipradeep.code.repositories.OrderRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class OrderStateService {

    private final EntityManager em;
    private final EntityStateDetector stateDetector;
    private final OrderRepository orderRepo;

    public OrderStateService(EntityManager em, EntityStateDetector stateDetector, OrderRepository orderRepo) {
        this.em = em;
        this.stateDetector = stateDetector;
        this.orderRepo = orderRepo;
    }

    public void demonstrateStates() {
        // 1. TRANSIENT
        Order2 newOrder = new Order2("Laptop", 1, 999.99);
        logState("After instantiation", newOrder);

        // 2. MANAGED (after persist)
        em.persist(newOrder);
        logState("After persist", newOrder);

        // 3. SELECT from DB
        Order2 foundOrder = em.find(Order2.class, newOrder.getId());
        logState("After find", foundOrder);

        // 4. DETACHED (after clear)
        em.flush();
        em.clear();
        logState("After clear", newOrder);

        // 5. MANAGED again (after merge)
        Order2 mergedOrder = em.merge(newOrder);
        logState("After merge", mergedOrder);

        // 6. DELETE from DB
        em.remove(mergedOrder);
        logState("After remove", mergedOrder);

        // 7. Verify deletion
        Order2 deletedOrder = em.find(Order2.class, mergedOrder.getId());
        logState("After find deleted", deletedOrder); // Will be null
    }

    public void deleteOrderFromDatabase(UUID orderId) {
        // 1. Find the order (will be MANAGED if exists)
        Order2 order = em.find(Order2.class, orderId);
        logState("Before delete", order);

        if (order != null) {
            // 2. Remove the order
            em.remove(order);
            logState("After delete", order);

            // 3. Flush changes to database immediately
            em.flush();

            // 4. Verify deletion
            Order2 verify = em.find(Order2.class, orderId);
            System.out.println("Verification after delete: " + (verify == null ? "SUCCESS" : "FAILED"));
        }
    }

    private void logState(String action, Order2 order) {
        System.out.printf("[ENTITY STATE] %-20s | ID: %-36s | State: %-8s%n",
                action,
                order==null?"null":order.getId(),
                stateDetector.getEntityState(order));
    }

}