package com.invictus.territory.manager;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

public class InventoryManager implements Listener {

    private final TerritoryCore plugin;
    private final Map<String, InventoryData> inventories = new HashMap<>();
    private final Map<UUID, String> playerCurrentMenu = new HashMap<>();

    public InventoryManager(TerritoryCore plugin) {
        this.plugin = plugin;
        loadInventories();
    }

    private void loadInventories() {
        File invFolder = new File(plugin.getDataFolder(), "Inventarios");
        if (!invFolder.exists()) {
            invFolder.mkdirs();
        }

        // Cargar inventarios desde archivos
        loadInventoryFile("territorios.yml");
        loadInventoryFile("grecia.yml");
        loadInventoryFile("egipto.yml");
    }

    private void loadInventoryFile(String fileName) {
        File file = new File(plugin.getDataFolder(), "Inventarios/" + fileName);

        if (!file.exists()) {
            plugin.saveResource("Inventarios/" + fileName, false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String name = config.getString("name", "Menu");
        int size = config.getInt("size", 54);

        InventoryData invData = new InventoryData(name, size);
        ConfigurationSection itemsSection = config.getConfigurationSection("items");

        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection == null) continue;

                ConfigurationSection itemConfig = itemSection.getConfigurationSection("item");
                if (itemConfig == null) continue;

                String materialStr = itemConfig.getString("material", "STONE");
                Material material = Material.matchMaterial(materialStr);
                if (material == null) material = Material.STONE;

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                if (meta != null) {
                    String displayName = itemConfig.getString("name");
                    if (displayName != null) {
                        meta.displayName(MessageUtils.colorizeLegacy(displayName));
                    }

                    List<String> loreStrings = itemConfig.getStringList("lore");
                    if (!loreStrings.isEmpty()) {
                        List<Component> lore = new ArrayList<>();
                        for (String line : loreStrings) {
                            lore.add(MessageUtils.colorizeLegacy(line));
                        }
                        meta.lore(lore);
                    }

                    item.setItemMeta(meta);
                }

                // Obtener slots
                List<Integer> slots = new ArrayList<>();
                Object slotsObj = itemSection.get("slots");
                Object slotObj = itemSection.get("slot");

                if (slotsObj instanceof List) {
                    for (Object slotRange : (List<?>) slotsObj) {
                        if (slotRange instanceof String) {
                            String range = (String) slotRange;
                            if (range.contains("-")) {
                                String[] parts = range.split("-");
                                int start = Integer.parseInt(parts[0]);
                                int end = Integer.parseInt(parts[1]);
                                for (int i = start; i <= end; i++) {
                                    slots.add(i);
                                }
                            } else {
                                slots.add(Integer.parseInt(range));
                            }
                        } else if (slotRange instanceof Integer) {
                            slots.add((Integer) slotRange);
                        }
                    }
                } else if (slotObj instanceof Integer) {
                    slots.add((Integer) slotObj);
                }

                // Guardar acción del item
                String action = itemConfig.getString("action");
                for (int slot : slots) {
                    invData.items.put(slot, item);
                    if (action != null) {
                        invData.actions.put(slot, action);
                    }
                }
            }
        }

        String menuId = fileName.replace(".yml", "");
        inventories.put(menuId, invData);
        plugin.getLogger().info("§a✔ Inventario cargado: " + menuId + " (" + name + ")");
    }

    public void openMainMenu(Player player) {
        openMenu(player, "territorios");
    }

    public void openMenu(Player player, String menuId) {
        InventoryData invData = inventories.get(menuId);
        if (invData == null) {
            player.sendMessage(MessageUtils.colorize("&c❌ Menú no encontrado: " + menuId));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, invData.size, MessageUtils.colorizeLegacy(invData.name));

        for (Map.Entry<Integer, ItemStack> entry : invData.items.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }

        player.openInventory(inv);
        playerCurrentMenu.put(player.getUniqueId(), menuId);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        UUID playerId = player.getUniqueId();
        String currentMenu = playerCurrentMenu.get(playerId);

        if (currentMenu == null) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();
        InventoryData invData = inventories.get(currentMenu);

        if (invData == null) return;

        String action = invData.actions.get(slot);
        if (action == null) return;

        player.closeInventory();
        playerCurrentMenu.remove(playerId);

        // Procesar acciones
        if (action.startsWith("menu:")) {
            String targetMenu = action.substring(5);
            openMenu(player, targetMenu);
        } else if (action.startsWith("teleport:")) {
            String territoryId = action.substring(9);
            Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);
            if (territory != null) {
                plugin.getTerritoryManager().teleportToTerritory(player, territory);
            } else {
                player.sendMessage(MessageUtils.colorize("&c❌ Territorio no encontrado"));
            }
        } else if (action.startsWith("command:")) {
            String command = action.substring(8);
            player.performCommand(command);
        }
    }

    public void cleanup(Player player) {
        playerCurrentMenu.remove(player.getUniqueId());
    }

    private static class InventoryData {
        String name;
        int size;
        Map<Integer, ItemStack> items = new HashMap<>();
        Map<Integer, String> actions = new HashMap<>();

        InventoryData(String name, int size) {
            this.name = name;
            this.size = size;
        }
    }
}
