package com.invictus.territory.model;

import com.invictus.territory.model.enums.TerritoryCategory;
import com.invictus.territory.model.enums.TerritoryFaction;
import com.invictus.territory.model.enums.TerritorySize;

import java.util.UUID;

public class Territory {
    private String id;
    private String displayName;
    private TerritorySize size;
    private TerritoryCategory category;
    private TerritoryFaction faction;
    private BoundingBox boundingBox;
    private boolean owned;
    private UUID ownerClanId;
    private int level;
    private long upkeepBase;
    private long production;

    public Territory(String id, String displayName, TerritoryFaction faction, TerritoryCategory category,
                     TerritorySize size, BoundingBox boundingBox) {
        this.id = id;
        this.displayName = displayName;
        this.faction = faction;
        this.category = category;
        this.size = size;
        this.boundingBox = boundingBox;
        this.owned = false;
        this.ownerClanId = null;
        this.level = 1;
        this.upkeepBase = 1000;
        this.production = 100;
    }

    public Territory(String id, String displayName, TerritorySize size, TerritoryCategory category,
                     TerritoryFaction faction, BoundingBox boundingBox) {
        this.id = id;
        this.displayName = displayName;
        this.size = size;
        this.category = category;
        this.faction = faction;
        this.boundingBox = boundingBox;
        this.owned = false;
        this.ownerClanId = null;
        this.level = 1;
        this.upkeepBase = 1000;
    }

    // ===== GETTERS =====

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public TerritorySize getSize() {
        return size;
    }

    public TerritoryCategory getCategory() {
        return category;
    }

    public TerritoryFaction getFaction() {
        return faction;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public boolean isOwned() {
        return owned;
    }

    public UUID getOwnerClanId() {
        return ownerClanId;
    }

    public int getLevel() {
        return level;
    }

    public long getUpkeepBase() {
        return upkeepBase;
    }

    public long getProduction() {
        return production;
    }

    // ===== SETTERS =====

    public void setId(String id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSize(TerritorySize size) {
        this.size = size;
    }

    public void setCategory(TerritoryCategory category) {
        this.category = category;
    }

    public void setFaction(TerritoryFaction faction) {
        this.faction = faction;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public void setOwnerClanId(UUID ownerClanId) {
        this.ownerClanId = ownerClanId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUpkeepBase(long upkeepBase) {
        this.upkeepBase = upkeepBase;
    }

    public void setProduction(long production) {
        this.production = production;
    }

    @Override
    public String toString() {
        return "Territory{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", size=" + size +
                ", category=" + category +
                ", faction=" + faction +
                ", owned=" + owned +
                ", ownerClanId=" + ownerClanId +
                ", level=" + level +
                '}';
    }
}