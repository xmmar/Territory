package com.invictus.territory.gui;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.model.enums.TerritoryFaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TerritoryGUI {

    private final TerritoryCore plugin;

    public TerritoryGUI(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    // GUI para Territorios Griegos
    public void openGreciaGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&3&l&oG&b&l&oR&3&l&oE&b&l&oC&3&l&oI&b&l&oA"));

        int slot = 0;
        List<Territory> territorios = new ArrayList<>();

        for (Territory t : plugin.getTerritoryManager().getAllTerritories()) {
            if (t.getFaction().toString().equals("GREECE")) {
                territorios.add(t);
            }
        }

        for (Territory territorio : territorios) {
            if (slot >= 36) break;

            ItemStack item = createTerritoryItem(territorio, Material.EMERALD);
            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }

    // GUI para Territorios Egipcios
    public void openEgyptoGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&6&l&oE&c&l&oG&6&l&oI&c&l&oP&6&l&oT&c&l&oO"));

        int slot = 0;
        List<Territory> territorios = new ArrayList<>();

        for (Territory t : plugin.getTerritoryManager().getAllTerritories()) {
            if (t.getFaction().toString().equals("EGYPT")) {
                territorios.add(t);
            }
        }

        for (Territory territorio : territorios) {
            if (slot >= 36) break;

            ItemStack item = createTerritoryItem(territorio, Material.GOLD_BLOCK);
            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }

    // GUI para Territorios Centrales
    public void openCentralGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.translateAlternateColorCodes('&', "&b&l&oCENTRAL"));

        int slot = 0;
        List<Territory> territorios = new ArrayList<>();

        for (Territory t : plugin.getTerritoryManager().getAllTerritories()) {
            if (t.getFaction().toString().equals("NEUTRAL") && !t.getCategory().toString().equals("CAPITAL")) {
                territorios.add(t);
            }
        }

        for (Territory territorio : territorios) {
            if (slot >= 36) break;

            ItemStack item = createTerritoryItem(territorio, Material.ICE);
            inv.setItem(slot, item);
            slot++;
        }

        player.openInventory(inv);
    }

    // GUI para El Trono
    public void openTronoGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&4&l&oTRONO"));

        for (Territory t : plugin.getTerritoryManager().getAllTerritories()) {
            if (t.getCategory().toString().equals("CAPITAL")) {
                ItemStack item = createTerritoryItem(t, Material.REDSTONE_BLOCK);
                inv.setItem(0, item);
                break;
            }
        }

        player.openInventory(inv);
    }

    private ItemStack createTerritoryItem(Territory territory, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§6§l" + territory.getDisplayName());

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Tamaño: " + ChatColor.WHITE + territory.getSize().name());
            lore.add(ChatColor.GRAY + "Nivel: " + ChatColor.WHITE + territory.getLevel());
            lore.add(ChatColor.GRAY + "Upkeep: " + ChatColor.WHITE + "$" + territory.getUpkeepBase());

            if (territory.isOwned()) {
                lore.add(ChatColor.RED + "Estado: Conquistado");
                lore.add(ChatColor.RED + "Propietario: " + territory.getOwnerClanId());
            } else {
                lore.add(ChatColor.GREEN + "Estado: Disponible");
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return item;
    }
}