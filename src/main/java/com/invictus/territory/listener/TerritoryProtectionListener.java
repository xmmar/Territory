package com.invictus.territory.listener;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.model.BoundingBox;
import com.invictus.territory.util.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TerritoryProtectionListener implements Listener {

    private final TerritoryCore plugin;

    public TerritoryProtectionListener(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getBlock().getLocation();

        Territory territory = getTerritoryAt(loc);

        if (territory == null) return;

        // Los admins pueden romper bloques en cualquier lado
        if (player.hasPermission("territorio.admin")) return;

        // Si el territorio está sin dueño, permite romper
        if (!territory.isOwned()) return;

        // Si el clan del jugador es dueño del territorio, permite
        if (isTerritoryOwner(player, territory)) return;

        // Si no es dueño, cancela el evento
        event.setCancelled(true);
        player.sendMessage(MessageUtils.colorize("&c❌ No puedes romper bloques en territorio de otro clan"));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getBlock().getLocation();

        Territory territory = getTerritoryAt(loc);

        if (territory == null) return;

        if (player.hasPermission("territorio.admin")) return;

        if (!territory.isOwned()) return;

        if (isTerritoryOwner(player, territory)) return;

        event.setCancelled(true);
        player.sendMessage(MessageUtils.colorize("&c❌ No puedes colocar bloques en territorio de otro clan"));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        Location victimLoc = victim.getLocation();
        Territory territory = getTerritoryAt(victimLoc);

        if (territory == null) return;

        if (attacker.hasPermission("territorio.admin")) return;

        // Si el territorio no está reclamado, permite PvP
        if (!territory.isOwned()) return;

        // Si ambos son del mismo clan, no permite daño
        if (isSameClan(attacker, victim)) {
            event.setCancelled(true);
            attacker.sendMessage(MessageUtils.colorize("&c❌ No puedes atacar a miembros de tu clan"));
            return;
        }

        // PvP permitido entre clanes diferentes en territorios reclamados
    }

    private Territory getTerritoryAt(Location loc) {
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            BoundingBox bbox = territory.getBoundingBox();

            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();

            if (x >= bbox.getX1() && x <= bbox.getX2() &&
                    y >= bbox.getY1() && y <= bbox.getY2() &&
                    z >= bbox.getZ1() && z <= bbox.getZ2()) {
                return territory;
            }
        }
        return null;
    }

    private boolean isTerritoryOwner(Player player, Territory territory) {
        // TODO: Verificar con ChronicleClans si el jugador es miembro del clan dueño
        // Por ahora, retorna true para permitir construcción
        return true;
    }

    private boolean isSameClan(Player player1, Player player2) {
        // TODO: Verificar con ChronicleClans si pertenecen al mismo clan
        // Por ahora, retorna false
        return false;
    }
}