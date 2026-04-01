package com.invictus.territory.listener;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UserGUIListener implements Listener {

    private final TerritoryCore plugin;

    public UserGUIListener(TerritoryCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        Component titleComponent = event.getView().title();
        String title = PlainTextComponentSerializer.plainText().serialize(titleComponent);

        if (!title.contains("GRECIA") && !title.contains("EGIPTO") &&
                !title.contains("CENTRAL") && !title.contains("TRONO")) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;

        String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
        Territory territory = null;

        for (Territory t : plugin.getTerritoryManager().getAllTerritories()) {
            if (ChatColor.stripColor(t.getDisplayName()).equals(itemName)) {
                territory = t;
                break;
            }
        }

        if (territory != null) {
            player.closeInventory();
            showTerritoryInfo(player, territory);
        }
    }

    private void showTerritoryInfo(Player player, Territory territory) {
        player.sendMessage(MessageUtils.colorizeLegacy("\n§6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(MessageUtils.colorizeLegacy("§e📍 INFORMACIÓN DEL TERRITORIO"));
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorizeLegacy("§e● Nombre: §f" + territory.getDisplayName()));
        player.sendMessage(MessageUtils.colorizeLegacy("§e● Tamaño: §f" + territory.getSize().name()));
        player.sendMessage(MessageUtils.colorizeLegacy("§e● Nivel: §f" + territory.getLevel()));
        player.sendMessage(MessageUtils.colorizeLegacy("§e● Upkeep: §f$" + territory.getUpkeepBase()));
        player.sendMessage("");

        if (territory.isOwned()) {
            player.sendMessage(MessageUtils.colorizeLegacy("§c● Estado: CONQUISTADO"));
            player.sendMessage(MessageUtils.colorizeLegacy("§c● Dueño: §f" + territory.getOwnerClanId()));
        } else {
            player.sendMessage(MessageUtils.colorizeLegacy("§a● Estado: DISPONIBLE"));
        }

        player.sendMessage("");

        com.invictus.territory.model.BoundingBox bbox = territory.getBoundingBox();
        int centerX = ((int) bbox.getX1() + (int) bbox.getX2()) / 2;
        int centerZ = ((int) bbox.getZ1() + (int) bbox.getZ2()) / 2;

        player.sendMessage(MessageUtils.colorizeLegacy("§6● Coordenadas: §e" + centerX + " , " + centerZ));
        player.sendMessage(MessageUtils.colorizeLegacy("§6━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"));
    }
}