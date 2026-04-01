package com.invictus.territory.command;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

public class AdminTerritoryCommand implements CommandExecutor {

    private final TerritoryCore plugin;
    private final Component PREFIX;

    public AdminTerritoryCommand(TerritoryCore plugin) {
        this.plugin = plugin;
        String prefixConfig = plugin.getConfig().getString("prefixes.admin", "&4[&cAdmin&4]");
        this.PREFIX = MessageUtils.colorize(prefixConfig);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&c❌ Solo jugadores pueden usar este comando"));
            return true;
        }

        if (!player.hasPermission("territorio.admin")) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ No tienes permisos")));
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "tp" -> handleTeleport(player, args);
            case "claim" -> handleAdminClaim(player, args);
            case "unclaim" -> handleAdminUnclaim(player, args);
            case "info" -> handleInfo(player, args);
            case "list" -> handleList(player);
            case "reload" -> handleReload(player);
            default -> showHelp(player);
        }

        return true;
    }

    /**
     * Teleporta a un jugador a un territorio
     */
    private void handleTeleport(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Uso: /tadmin tp <nombre_territorio>")));
            return;
        }

        String territoryName = args[1];
        Territory territory = findTerritoryByName(territoryName);

        if (territory == null) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Territorio no encontrado: " + territoryName)));
            return;
        }

        if (territory.getBoundingBox() == null) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ El territorio no tiene una ubicación establecida")));
            return;
        }

        Location center = territory.getBoundingBox().getCenter();
        player.teleport(center);

        player.sendMessage(PREFIX.append(MessageUtils.colorize(" &a✅ Teleportado a: " + territory.getDisplayName())));

        // Usar la nueva API de Adventure para títulos
        Component titleComponent = MessageUtils.colorizeLegacy("§a" + territory.getDisplayName());
        Component subtitleComponent = MessageUtils.colorizeLegacy("§7Bienvenido");
        player.showTitle(Title.title(titleComponent, subtitleComponent));
    }

    /**
     * Reclama un territorio como admin
     */
    private void handleAdminClaim(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Uso: /tadmin claim <territorio> <clan_uuid>")));
            return;
        }

        String territoryId = args[1];
        UUID clanId;

        try {
            clanId = UUID.fromString(args[2]);
        } catch (IllegalArgumentException e) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ UUID inválido: " + args[2])));
            return;
        }

        boolean success = plugin.getClaimManager().claimTerritory(territoryId, clanId);

        if (success) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &a✅ Territorio reclamado exitosamente")));
            Bukkit.broadcast(PREFIX.append(MessageUtils.colorize(" &6🏴 Admin ha reclamado territorio: " + territoryId)));
        } else {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ No se pudo reclamar el territorio")));
        }
    }

    /**
     * Libera un territorio como admin
     */
    private void handleAdminUnclaim(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Uso: /tadmin unclaim <territorio>")));
            return;
        }

        String territoryId = args[1];
        plugin.getClaimManager().forceUnclaimTerritory(territoryId);

        player.sendMessage(PREFIX.append(MessageUtils.colorize(" &a✅ Territorio liberado")));
        Bukkit.broadcast(PREFIX.append(MessageUtils.colorize(" &6⚪ Admin ha liberado territorio: " + territoryId)));
    }

    /**
     * Muestra info de un territorio
     */
    private void handleInfo(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Uso: /tadmin info <territorio>")));
            return;
        }

        String territoryId = args[1];
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);

        if (territory == null) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Territorio no encontrado")));
            return;
        }

        String owner = territory.isOwned() ? territory.getOwnerClanId().toString() : "✓ Disponible";

        player.sendMessage("");
        player.sendMessage(PREFIX);
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(MessageUtils.colorize("&e📍 " + territory.getDisplayName()));
        player.sendMessage(MessageUtils.colorize("&7ID: &f" + territory.getId()));
        player.sendMessage(MessageUtils.colorize("&7Tamaño: &f" + territory.getSize().name()));
        player.sendMessage(MessageUtils.colorize("&7Categoría: &f" + territory.getCategory().getDisplayName()));
        player.sendMessage(MessageUtils.colorize("&7Facción: &f" + territory.getFaction().getDisplayName()));
        player.sendMessage(MessageUtils.colorize("&7Nivel: &f" + territory.getLevel()));
        player.sendMessage(MessageUtils.colorize("&7Upkeep: &f" + territory.getUpkeepBase()));
        player.sendMessage(MessageUtils.colorize("&7Dueño: &f" + owner));
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage("");
    }

    /**
     * Lista todos los territorios
     */
    private void handleList(Player player) {
        player.sendMessage("");
        player.sendMessage(PREFIX);
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(MessageUtils.colorize("&e📋 TODOS LOS TERRITORIOS"));
        player.sendMessage("");

        int total = 0;
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            String ownerText = territory.isOwned() ? "§c" + territory.getOwnerClanId() : "§a✓ Disponible";
            player.sendMessage(MessageUtils.colorize("&7• " + territory.getDisplayName() + " - ").append(MessageUtils.colorizeLegacy(ownerText)));
            total++;
        }

        player.sendMessage("");
        player.sendMessage(MessageUtils.colorize("&eTotal: &f" + total));
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage("");
    }

    /**
     * Recarga la configuración
     */
    private void handleReload(Player player) {
        plugin.reloadConfig();
        player.sendMessage(PREFIX.append(MessageUtils.colorize(" &a✅ Configuración recargada")));
    }

    /**
     * Busca un territorio por nombre
     */
    private Territory findTerritoryByName(String name) {
        for (Territory territory : plugin.getTerritoryManager().getAllTerritories()) {
            if (territory.getDisplayName().equalsIgnoreCase(name) ||
                    territory.getId().equalsIgnoreCase(name)) {
                return territory;
            }
        }
        return null;
    }

    private void showHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(PREFIX);
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(MessageUtils.colorize("&e📋 COMANDOS ADMIN"));
        player.sendMessage(MessageUtils.colorize("&7/tadmin tp <nombre>               &f- Teleportarse a territorio"));
        player.sendMessage(MessageUtils.colorize("&7/tadmin claim <id> <uuid>         &f- Reclamar territorio"));
        player.sendMessage(MessageUtils.colorize("&7/tadmin unclaim <id>              &f- Liberar territorio"));
        player.sendMessage(MessageUtils.colorize("&7/tadmin info <id>                 &f- Ver info territorio"));
        player.sendMessage(MessageUtils.colorize("&7/tadmin list                      &f- Listar todos"));
        player.sendMessage(MessageUtils.colorize("&7/tadmin reload                    &f- Recargar config"));
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage("");
    }
}