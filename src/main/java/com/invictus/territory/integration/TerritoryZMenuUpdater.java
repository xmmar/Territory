package com.invictus.territory.integration;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.data.TerritoryData;
import com.invictus.territory.model.Territory;
import com.invictus.territory.model.BoundingBox;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TerritoryZMenuUpdater implements Listener {

    private final TerritoryCore plugin;

    public TerritoryZMenuUpdater(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        String title = String.valueOf(inv.getType());

        if (title.contains("Grecia")) {
            updateGreciaMenu(inv);
        } else if (title.contains("Egipto")) {
            updateEgyptoMenu(inv);
        } else if (title.contains("Central")) {
            updateCentralMenu(inv);
        }
    }

    private void updateGreciaMenu(Inventory inv) {
        int slotIndex = 0;
        for (TerritoryData.TerritoryInfo info : TerritoryData.GREECE_TERRITORIES) {
            Territory territory = plugin.getTerritoryManager().getTerritory(info.id);
            if (territory != null) {
                int slot = getSlotForTerritory(slotIndex);
                ItemStack item = createTerritoryItem(territory, info);
                inv.setItem(slot, item);
                slotIndex++;
            }
        }
    }

    private void updateEgyptoMenu(Inventory inv) {
        int slotIndex = 0;
        for (TerritoryData.TerritoryInfo info : TerritoryData.EGYPT_TERRITORIES) {
            Territory territory = plugin.getTerritoryManager().getTerritory(info.id);
            if (territory != null) {
                int slot = getSlotForTerritory(slotIndex);
                ItemStack item = createTerritoryItem(territory, info);
                inv.setItem(slot, item);
                slotIndex++;
            }
        }
    }

    private void updateCentralMenu(Inventory inv) {
        int slotIndex = 0;
        for (TerritoryData.TerritoryInfo info : TerritoryData.CENTRAL_TERRITORIES) {
            Territory territory = plugin.getTerritoryManager().getTerritory(info.id);
            if (territory != null) {
                int slot = getSlotForTerritory(slotIndex);
                ItemStack item = createTerritoryItem(territory, info);
                inv.setItem(slot, item);
                slotIndex++;
            }
        }
    }

    private ItemStack createTerritoryItem(Territory territory, TerritoryData.TerritoryInfo info) {
        ItemStack item = new ItemStack(info.biome == org.bukkit.block.Biome.FOREST ?
                org.bukkit.Material.HEART_OF_THE_SEA : org.bukkit.Material.EMERALD_BLOCK);

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(MessageUtils.colorizeLegacy("§a§l" + info.displayName));

            List<Component> lore = new ArrayList<>();
            lore.add(MessageUtils.colorizeLegacy("§8" + info.description));
            lore.add(Component.empty());
            lore.add(MessageUtils.colorizeLegacy("§e" + info.tag));
            lore.add(Component.empty());

            BoundingBox bbox = territory.getBoundingBox();
            int centerX = (int) ((bbox.getX1() + bbox.getX2()) / 2);
            int centerZ = (int) ((bbox.getZ1() + bbox.getZ2()) / 2);

            lore.add(MessageUtils.colorizeLegacy("§7X: §f" + centerX + " §7Z: §f" + centerZ));
            lore.add(Component.empty());

            if (territory.isOwned()) {
                lore.add(MessageUtils.colorizeLegacy("§c✗ CONQUISTADO"));
                lore.add(MessageUtils.colorizeLegacy("§cDueño: §f" + territory.getOwnerClanId()));
            } else {
                lore.add(MessageUtils.colorizeLegacy("§a✓ DISPONIBLE"));
            }

            meta.lore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Calcula el slot basado en el índice del territorio
     */
    private int getSlotForTerritory(int index) {
        // Slots para grecia: 10-15 (primera fila de territorios)
        // Luego 19-24 (segunda fila)
        if (index < 6) {
            return 10 + index;
        } else if (index < 12) {
            return 19 + (index - 6);
        }
        return 10; // Por defecto
    }
}