package com.invictus.territory.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TerritoryEnterEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String territoryId;

    public TerritoryEnterEvent(Player player, String territoryId) {
        this.player = player;
        this.territoryId = territoryId;
    }

    public Player getPlayer() { return player; }
    public String getTerritoryId() { return territoryId; }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; }
}