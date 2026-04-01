package com.invictus.territory.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.UUID;

public class TerritoryUpkeepEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final UUID clanId;
    private final long totalCost;
    private final boolean successful;

    public TerritoryUpkeepEvent(UUID clanId, long totalCost, boolean successful) {
        this.clanId = clanId;
        this.totalCost = totalCost;
        this.successful = successful;
    }

    public UUID getClanId() { return clanId; }
    public long getTotalCost() { return totalCost; }
    public boolean isSuccessful() { return successful; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}