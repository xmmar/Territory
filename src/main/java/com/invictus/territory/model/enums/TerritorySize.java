package com.invictus.territory.model.enums;

public enum TerritorySize {
    SMALL(60, 5000, 100),
    MEDIUM(90, 12000, 150),
    LARGE(120, 25000, 200),
    COLOSSAL(180, 50000, 300);

    private final int minRaidDuration;
    private final long baseUpkeep;
    private final int baseRadius;

    TerritorySize(int minRaidDuration, long baseUpkeep, int baseRadius) {
        this.minRaidDuration = minRaidDuration;
        this.baseUpkeep = baseUpkeep;
        this.baseRadius = baseRadius;
    }

    public int getMinRaidDuration() {
        return minRaidDuration;
    }

    public long getBaseUpkeep() {
        return baseUpkeep;
    }

    public int getBaseRadius() {
        return baseRadius;
    }
}