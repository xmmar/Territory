package com.invictus.territory.model.enums;

/**
 * Niveles de territorio con multiplicadores de producción y upkeep
 */
public enum TerritoryLevel {
    LEVEL_I(1, 1.0, 1.0, "§7Nivel I"),
    LEVEL_II(2, 1.4, 2.3, "§aNivel II"),
    LEVEL_III(3, 2.0, 4.5, "§eNivel III");

    private final int level;
    private final double productionMultiplier;
    private final double upkeepMultiplier;
    private final String displayName;

    TerritoryLevel(int level, double productionMultiplier, double upkeepMultiplier, String displayName) {
        this.level = level;
        this.productionMultiplier = productionMultiplier;
        this.upkeepMultiplier = upkeepMultiplier;
        this.displayName = displayName;
    }

    public int getLevel() {
        return level;
    }

    public double getProductionMultiplier() {
        return productionMultiplier;
    }

    public double getUpkeepMultiplier() {
        return upkeepMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Obtiene el TerritoryLevel según el nivel numérico
     *
     * @param level Nivel del territorio (1-100)
     * @return TerritoryLevel correspondiente
     */
    public static TerritoryLevel fromLevel(int level) {
        if (level <= 33) return LEVEL_I;
        if (level <= 66) return LEVEL_II;
        return LEVEL_III;
    }

    /**
     * Calcula la producción ajustada según el nivel
     *
     * @param baseProduction Producción base del territorio
     * @param level          Nivel actual del territorio
     * @return Producción ajustada
     */
    public static long calculateProduction(long baseProduction, int level) {
        TerritoryLevel territoryLevel = fromLevel(level);
        return (long) (baseProduction * territoryLevel.getProductionMultiplier());
    }

    /**
     * Calcula el upkeep ajustado según el nivel
     *
     * @param baseUpkeep Upkeep base del territorio
     * @param level      Nivel actual del territorio
     * @return Upkeep ajustado
     */
    public static long calculateUpkeep(long baseUpkeep, int level) {
        TerritoryLevel territoryLevel = fromLevel(level);
        return (long) (baseUpkeep * territoryLevel.getUpkeepMultiplier());
    }
}
