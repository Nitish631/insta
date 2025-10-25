package com.nitish.insta.Entities;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ReactionType {
    LIKE(1, "LIKE"),
    LOVE(2, "LOVE"),
    LAUGH(3, "LAUGH"),
    WOW(4, "WOW"),
    SAD(5, "SAD"),
    ANGRY(6, "ANGRY"),
    FIRE(7, "FIRE");

    private final int id;
    private final String displayName;

    ReactionType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Find ReactionType by id
    public static ReactionType fromId(int id) {
        for (ReactionType type : values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ReactionType id: " + id);
    }

    // Convert to Map (for APIs or frontend)
    public static Map<Integer, String> toMap() {
        Map<Integer, String> map = new LinkedHashMap<>();
        for (ReactionType type : values()) {
            map.put(type.getId(), type.getDisplayName());
        }
        return map;
    }
}
