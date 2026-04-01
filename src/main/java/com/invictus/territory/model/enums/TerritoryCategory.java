package com.invictus.territory.model.enums;

public enum TerritoryCategory {
    STRATEGIC("Estratégico"),
    PVE("PvE"),
    ECONOMIC("Económico"),
    CAPITAL("Capital");

    private final String displayName;

    TerritoryCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}