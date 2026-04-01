package com.invictus.territory.model.enums;

public enum TerritoryFaction {
    GREECE("Grecia"),
    EGYPT("Egipto"),
    NEUTRAL("Neutral");

    private final String displayName;

    TerritoryFaction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}