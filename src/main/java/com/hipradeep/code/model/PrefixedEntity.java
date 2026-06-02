package com.hipradeep.code.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * New entity configured with custom Prefixed UUID generator strategy.
 */
@Entity
@Table(name = "prefixed_entities")
public class PrefixedEntity {

    @Id
    @GeneratedValue(generator = "prefixed-uuid")
    @GenericGenerator(
            name = "prefixed-uuid",
            strategy = "com.hipradeep.code.jpa.PrefixedUUIDGenerator",
            parameters = {
                    @Parameter(name = "prefix", value = "PROD_"),
                    @Parameter(name = "length", value = "32")
            }
    )
    private String id;

    private String name;

    public PrefixedEntity() {
    }

    public PrefixedEntity(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
