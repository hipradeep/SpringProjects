package com.hipradeep.code.utils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class CompositeId implements Serializable {
    private String userId;
    private Integer entryId;

    // Default constructor, equals, and hashCode methods
    public CompositeId() {
    }

    public CompositeId(String userId, Integer entryId) {
        this.userId = userId;
        this.entryId = entryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeId userId1 = (CompositeId) o;
        return userId.equals(userId1.userId) && entryId.equals(userId1.entryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, entryId);
    }
}