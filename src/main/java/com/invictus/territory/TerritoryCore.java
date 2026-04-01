package com.invictus.territory;

import com.invictus.territory.command.AdminTerritoryCommand;
import com.invictus.territory.command.TerritoryCommand;
import com.invictus.territory.integration.*;
import com.invictus.territory.listener.*;
import com.invictus.territory.loader.TerritoryLoader;
import com.invictus.territory.manager.RaidWindowManager;
import com.invictus.territory.manager.TerritoryClaimManager;
import com.invictus.territory.manager.TerritoryManager;
import com.invictus.territory.manager.TerritoryMobManager;
import com.invictus.territory.manager.InventoryManager;
import com.invictus.territory.model.Territory;
import com.invictus.territory.scheduler.UpkeepScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TerritoryCore extends JavaPlugin {

    private TerritoryManager territoryManager;
    private RaidWindowManager raidWindowManager;
    private TerritoryClaimManager claimManager;
    private UpkeepScheduler upkeepScheduler;
    private TerritoryMobManager mobManager;
    private InventoryManager inventoryManager;

    @Override
    public void onEnable() {
        try {
            printBanner();
            getLogger().info("§8[§6⚔§8] §7Iniciando sistema de territorios...");

            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
            }

            saveDefaultConfig();
            saveResource("territories.yml", false);

            // Inicializar managers
            getLogger().info("§8[§6⚔§8] §7Inicializando managers...");
            territoryManager = new TerritoryManager(this);
            raidWindowManager = new RaidWindowManager(this);
            claimManager = new TerritoryClaimManager(this);
            upkeepScheduler = new UpkeepScheduler(this);
            mobManager = new TerritoryMobManager(this);
            inventoryManager = new InventoryManager(this);
            getLogger().info("§8[§a✔§8] §aManagers inicializados correctamente");

            loadTerritories();
            registerListeners();
            registerCommands();

            // Registrar mob manager
            Bukkit.getPluginManager().registerEvents(mobManager, this);
            getLogger().info("§8[§a✔§8] §aGestor de mobs temáticos activado");

            // Iniciar scheduler de upkeep
            upkeepScheduler.start();

            // Nota: Los schematics deben ser cargados manualmente usando WorldEdit
            // Coloca "Spawn Griego-whaky.schem" en /plugins/TerritoryCore/schematics/
            // Y usa el comando de WorldEdit para pegarlo en Monte Olimpo
            getLogger().info("§8[§eℹ§8] §7Para cargar schematics, usa WorldEdit manualmente");

            getLogger().info("");
            getLogger().info("§8[§a✔§8] §a§lPlugin habilitado correctamente");
            getLogger().info("§8[§6⚔§8] §6Chronicle Territory §elisto para usar");
            getLogger().info("");
        } catch (Exception e) {
            getLogger().severe("[TerritoryCore] ❌ Error al iniciar el plugin");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("[TerritoryCore] ❌ TerritoryCore deshabilitado");
    }

    private void loadTerritories() {
        try {
            TerritoryLoader loader = new TerritoryLoader(this);
            List<Territory> territories = loader.loadTerritories();

            for (Territory territory : territories) {
                territoryManager.addTerritory(territory);
            }

            getLogger().info("[TerritoryCore] ✅ " + territories.size() + " territorios cargados desde territories.yml");
        } catch (Exception e) {
            getLogger().severe("[TerritoryCore] ❌ Error cargando territorios");
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        try {
            Bukkit.getPluginManager().registerEvents(new TerritoryProtectionListener(this), this);
            Bukkit.getPluginManager().registerEvents(new TerritoryEventListener(this), this);
            Bukkit.getPluginManager().registerEvents(new TerritoryStatusListener(this), this);
            Bukkit.getPluginManager().registerEvents(inventoryManager, this);

            // Integraciones
            Bukkit.getPluginManager().registerEvents(new BiomeEffectsIntegration(this), this);
            Bukkit.getPluginManager().registerEvents(new AuraSkillsIntegration(this), this);
            Bukkit.getPluginManager().registerEvents(new LevelledMobsIntegration(this), this);

            getLogger().info("[TerritoryCore] ✅ Listeners registrados");
        } catch (Exception e) {
            getLogger().severe("[TerritoryCore] ❌ Error registrando listeners");
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        try {
            if (getCommand("territorios") != null) {
                getCommand("territorios").setExecutor(new TerritoryCommand(this));
                getLogger().info("[TerritoryCore] ✅ Comando /territorios registrado");
            } else {
                getLogger().warning("[TerritoryCore] ⚠️ Comando 'territorios' no encontrado en plugin.yml");
            }

            if (getCommand("tadmin") != null) {
                getCommand("tadmin").setExecutor(new AdminTerritoryCommand(this));
                getLogger().info("[TerritoryCore] ✅ Comando /tadmin registrado");
            } else {
                getLogger().warning("[TerritoryCore] ⚠️ Comando 'tadmin' no encontrado en plugin.yml");
            }
        } catch (Exception e) {
            getLogger().severe("[TerritoryCore] ❌ Error registrando comandos");
            e.printStackTrace();
        }
    }

    // Getters
    public TerritoryManager getTerritoryManager() {
        return territoryManager;
    }

    public RaidWindowManager getRaidWindowManager() {
        return raidWindowManager;
    }

    public TerritoryClaimManager getClaimManager() {
        return claimManager;
    }

    public UpkeepScheduler getUpkeepScheduler() {
        return upkeepScheduler;
    }

    public TerritoryMobManager getMobManager() {
        return mobManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    /**
     * Imprime el banner del plugin al iniciar
     */
    private void printBanner() {
        getLogger().info("§8╔═══════════════════════════════════════════════════╗");
        getLogger().info("§8║                                                   ║");
        getLogger().info("§8║     §6⚔ §e§lCHRONICLE §6§lTERRITORY §r§6⚔              §8║");
        getLogger().info("§8║                                                   ║");
        getLogger().info("§8║         §7Sistema Avanzado de Territorios         §8║");
        getLogger().info("§8║              §7Versión §e1.0.0                      §8║");
        getLogger().info("§8║                                                   ║");
        getLogger().info("§8║     §7Autor: §fxmmarMe                             §8║");
        getLogger().info("§8║     §7Paper: §f1.21.10+                            §8║");
        getLogger().info("§8║                                                   ║");
        getLogger().info("§8╚═══════════════════════════════════════════════════╝");
        getLogger().info("");
    }
}