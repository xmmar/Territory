package com.invictus.territory.listener;

import com.invictus.territory.TerritoryCore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUIListener implements Listener {

    private final TerritoryCore plugin;

    public GUIListener(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Inventory inv = event.getInventory();
        if (inv.getHolder() == null) return;

        String invTitle = inv.getHolder().getInventory().getHolder() != null ?
                inv.getHolder().getInventory().toString() : "";

        // Si hace click en el inventario principal
        if (invTitle.contains("Territorios")) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) return;

            String itemName = "";
            if (event.getCurrentItem().getItemMeta() != null) {
                Component displayName = event.getCurrentItem().getItemMeta().displayName();
                if (displayName != null) {
                    itemName = LegacyComponentSerializer.legacySection().serialize(displayName);
                }
            }

            switch (itemName) {
                case "§3Grecia" -> openMenu(player, "grecia");
                case "§6Egipto" -> openMenu(player, "egipto");
                case "§5Central" -> openMenu(player, "central");
                case "§4Trono" -> openMenu(player, "trono");
            }
        }
    }

    private void openMenu(Player player, String menuName) {
        try {
            player.performCommand("zmenu open " + menuName);
            plugin.getLogger().info("[TerritoryCore] ✅ Menú abierto: " + menuName);
        } catch (Exception e) {
            player.sendMessage("§cError al abrir el menú");
            plugin.getLogger().warning("[TerritoryCore] ❌ Error abriendo menú: " + menuName);
        }
    }
}