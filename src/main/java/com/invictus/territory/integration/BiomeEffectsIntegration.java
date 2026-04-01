package com.invictus.territory.integration;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.data.TerritoryData;
import com.invictus.territory.model.Territory;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BiomeEffectsIntegration implements Listener {

    private final TerritoryCore plugin;
    private final Map<UUID, String> playerBiomes = new HashMap<>();

    public BiomeEffectsIntegration(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        Territory territory = getTerritoryAt(player);

        if (territory == null) {
            playerBiomes.remove(playerId);
            return;
        }

        String currentBiome = territory.getId();
        String lastBiome = playerBiomes.get(playerId);

        if (!currentBiome.equals(lastBiome)) {
            playerBiomes.put(playerId, currentBiome);
            applyBiomeEffects(player, territory);
        }
    }

    /**
     * Aplica efectos visuales según el territorio
     */
    private void applyBiomeEffects(Player player, Territory territory) {
        TerritoryData.TerritoryInfo info = getTerritoryInfo(territory.getId());

        if (info == null) return;

        // Seleccionar partículas según territorio
        Particle particle = selectParticle(territory.getId());

        // Generar partículas alrededor del jugador
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > 100) {
                    cancel();
                    return;
                }

                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                double angle = (ticks * 3.6) * Math.PI / 180;
                double x = Math.cos(angle) * 2;
                double z = Math.sin(angle) * 2;

                player.spawnParticle(particle, player.getLocation().add(x, 1.5, z), 1);

                ticks++;
            }
        }.runTaskTimer(plugin, 0, 2);
    }

    /**
     * Selecciona partículas según el tipo de territorio
     */
    private Particle selectParticle(String territoryId) {
        return switch (territoryId) {
            case "monte_olimpo" -> Particle.END_ROD; // Serenidad
            case "esparta" -> Particle.FLAME; // Violencia
            case "bosque_artemisa" -> Particle.HAPPY_VILLAGER; // Naturaleza
            case "atenas_antigua" -> Particle.ENCHANT; // Economía
            case "acantilados_poseidon" -> Particle.FALLING_WATER; // Marino
            case "santuario_apolo" -> Particle.INSTANT_EFFECT; // Misiones
            case "ruinas_delfos" -> Particle.PORTAL; // Profecía
            case "valle_ares" -> Particle.REVERSE_PORTAL; // PvP
            case "coloso_rodas" -> Particle.EXPLOSION; // Boss
            case "islas_egeas" -> Particle.UNDERWATER;// Islas
            // EGIPTO
            case "piramide_keops" -> Particle.DUST; // Reliquias
            case "valle_reyes" -> Particle.FLAME; // Alto nivel
            case "oasis_bastet" -> Particle.DRIPPING_WATER; // Oasis
            case "desierto_set" -> Particle.SOUL_FIRE_FLAME; // Mobs agresivos
            case "templo_anubis" -> Particle.SOUL; // Nocturno
            case "rio_nilo" -> Particle.FALLING_DRIPSTONE_WATER; // Recursos
            case "biblioteca_thoth" -> Particle.END_ROD; // XP
            case "necropolis" -> Particle.SOUL; // Boss
            case "templo_solar_ra" -> Particle.FLAME; // Solar
            case "laberinto_sobek" -> Particle.PORTAL; // Dungeon
            // CENTRAL
            case "ruinas_imperio" -> Particle.EXPLOSION_EMITTER; // Legendario
            case "arena_titanes" -> Particle.REVERSE_PORTAL; // Clan War
            case "puerta_inframundo" -> Particle.SOUL_FIRE_FLAME; // Inframundo
            case "ciudad_perdida" -> Particle.ENCHANT; // Único
            case "trono_dominador" -> Particle.EXPLOSION_EMITTER; // Dominio
            default -> Particle.END_ROD;
        };
    }

    private TerritoryData.TerritoryInfo getTerritoryInfo(String territoryId) {
        for (TerritoryData.TerritoryInfo info : TerritoryData.GREECE_TERRITORIES) {
            if (info.id.equals(territoryId)) return info;
        }
        for (TerritoryData.TerritoryInfo info : TerritoryData.EGYPT_TERRITORIES) {
            if (info.id.equals(territoryId)) return info;
        }
        for (TerritoryData.TerritoryInfo info : TerritoryData.CENTRAL_TERRITORIES) {
            if (info.id.equals(territoryId)) return info;
        }
        return null;
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