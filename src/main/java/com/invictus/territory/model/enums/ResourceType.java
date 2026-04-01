package com.invictus.territory.model.enums;

public enum ResourceType {
    GOLD("Oro"),
    DIAMOND("Diamante"),
    IRON("Hierro"),
    EMERALD("Esmeralda"),
    EXPERIENCE("Experiencia");

    private final String displayName;

    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}