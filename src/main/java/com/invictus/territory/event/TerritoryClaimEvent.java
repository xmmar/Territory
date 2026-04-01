package com.invictus.territory.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import java.util.UUID;

public class TerritoryClaimEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String territoryId;
    private final UUID clanId;

    public TerritoryClaimEvent(String territoryId, UUID clanId) {
        this.territoryId = territoryId;
        this.clanId = clanId;
    }

    public String getTerritoryId() { return territoryId; }
    public UUID getClanId() { return clanId; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}