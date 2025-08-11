package com.hipradeep.code.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.engine.spi.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.Status;
import org.springframework.stereotype.Service;

@Service
public class EntityStateDetector {

    @PersistenceContext
    private EntityManager entityManager;
    public String getEntityState(Object entity) {

        Session session = entityManager.unwrap(Session.class);
        SessionImplementor sessionImplementor;

        if (session instanceof SessionImplementor) {
            sessionImplementor = (SessionImplementor) session;
        } else {
            sessionImplementor = session.unwrap(SessionImplementor.class);
        }

        org.hibernate.engine.spi.PersistenceContext persistenceContext =
                sessionImplementor.getPersistenceContext();

        EntityEntry entry = persistenceContext.getEntry(entity);



        if (entry == null) {
            return entityManager.getEntityManagerFactory()
                    .getPersistenceUnitUtil().isLoaded(entity)
                    ? "DETACHED"
                    : "TRANSIENT";
        }

        return switch (entry.getStatus()) {
            case MANAGED -> "MANAGED";
            case READ_ONLY -> "READ_ONLY";
            case DELETED -> "REMOVED";
            case GONE -> "GONE";
            case LOADING -> "LOADING";
            case SAVING -> "SAVING";
            default -> "DETACHED";
        };
    }


    public String getSimpleState(Object entity) {
        if (entityManager.contains(entity)) {
            return "MANAGED";
        } else if (entity instanceof HibernateProxy) {
            return "PROXY";
        } else {
            return entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(entity)
                    ? "DETACHED"
                    : "TRANSIENT";
        }
    }
}
