package com.invictus.territory.integration;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuraSkillsIntegration implements Listener {

    private final TerritoryCore plugin;
    private final Map<UUID, String> playerTerritories = new HashMap<>();

    public AuraSkillsIntegration(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        Territory territory = getTerritoryAt(player);

        if (territory == null) {
            playerTerritories.remove(playerId);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.RESISTANCE);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            return;
        }

        String currentTerritoryId = territory.getId();
        String lastTerritoryId = playerTerritories.get(playerId);

        if (!currentTerritoryId.equals(lastTerritoryId)) {
            playerTerritories.put(playerId, currentTerritoryId);
            applyTerritoryEffects(player, territory);
        }
    }

    /**
     * Aplica efectos según el territorio
     */
    private void applyTerritoryEffects(Player player, Territory territory) {
        String territoryId = territory.getId();

        // Limpiar efectos previos
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.RESISTANCE);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.STRENGTH);

        switch (territoryId) {
            case "monte_olimpo" -> {
                // Serenidad: Regeneración lenta
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
            }
            case "esparta" -> {
                // Violencia: Más daño
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, false, false));
            }
            case "bosque_artemisa" -> {
                // PvE: Velocidad
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            }
            case "atenas_antigua" -> {
                // Economía: Sin efectos especiales
            }
            case "acantilados_poseidon" -> {
                // Marino: Respiración acuática
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
            }
            case "santuario_apolo" -> {
                // Misiones: Buena suerte
                player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 0, false, false));
            }
            case "valle_ares" -> {
                // PvP brutal: Resistencia
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, false, false));
            }
            case "coloso_rodas" -> {
                // Boss: Resitencia + Regeneración
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0, false, false));
            }
            case "desierto_set" -> {
                // Peligroso: Sin efectos (riesgo real)
            }
            case "templo_anubis" -> {
                // Nocturno: Visión nocturna
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            }
            case "trono_dominador" -> {
                // Dominio: Todos los efectos
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
            }
        }
    }

    private Territory getTerritoryAt(Player player) {
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            com.invictus.territory.model.BoundingBox bbox = territory.getBoundingBox();

            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();

            if (x >= bbox.getX1() && x <= bbox.getX2() &&
                    y >= bbox.getY1() && y <= bbox.getY2() &&
                    z >= bbox.getZ1() && z <= bbox.getZ2()) {
                return territory;
            }
        }
        return null;
    }
}