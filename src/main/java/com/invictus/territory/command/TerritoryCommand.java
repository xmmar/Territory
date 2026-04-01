package com.invictus.territory.command;

import com.invictus.territory.TerritoryCore;
import com.invictus.territory.model.Territory;
import com.invictus.territory.util.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TerritoryCommand implements CommandExecutor {

    private final TerritoryCore plugin;
    private final Component PREFIX;

    public TerritoryCommand(TerritoryCore plugin) {
        this.plugin = plugin;
        String prefixConfig = plugin.getConfig().getString("prefixes.main", "&6[&eTerritorio&6]");
        this.PREFIX = MessageUtils.colorize(prefixConfig);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorize("&c❌ Solo jugadores pueden usar este comando"));
            return true;
        }

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "menu" -> plugin.getInventoryManager().openMainMenu(player);
            case "info" -> handleInfo(player, args);
            case "list" -> handleList(player);
            case "claim" -> handleClaim(player, args);
            case "unclaim" -> handleUnclaim(player, args);
            default -> showHelp(player);
        }

        return true;
    }


    private void handleInfo(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Uso: /territorios info <id_territorio>")));
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
        player.sendMessage(MessageUtils.colorize("&7Tamaño: &f" + territory.getSize().name()));
        player.sendMessage(MessageUtils.colorize("&7Categoría: &f" + territory.getCategory().getDisplayName()));
        player.sendMessage(MessageUtils.colorize("&7Facción: &f" + territory.getFaction().getDisplayName()));
        player.sendMessage(MessageUtils.colorize("&7Dueño: &f" + owner));
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage("");
    }

    private void handleList(Player player) {
        player.sendMessage("");
        player.sendMessage(PREFIX);
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(MessageUtils.colorize("&e📋 LISTA DE TERRITORIOS"));
        player.sendMessage("");

        int count = 0;
        for (Territory t : plugin.getTerritoryManager().getAllTerritories()) {
            String status = t.isOwned() ? "&c✖ Ocupado" : "&a✔ Libre";
            player.sendMessage(MessageUtils.colorize("&7- " + t.getDisplayName() + " " + status));
            count++;
        }

        player.sendMessage("");
        player.sendMessage(MessageUtils.colorize("&7Total: &f" + count + " territorios"));
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage("");
    }

    private void handleClaim(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Uso: /territorios claim <id_territorio>")));
            return;
        }

        if (!hasClan(player)) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Necesitas estar en un clan")));
            return;
        }

        String territoryId = args[1];
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);

        if (territory == null) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Territorio no encontrado")));
            return;
        }

        if (territory.isOwned()) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Este territorio ya está reclamado por: " + territory.getOwnerClanId())));
            return;
        }

        // Reclamar territorio
        UUID clanId = getClanId(player);
        territory.setOwned(true);
        territory.setOwnerClanId(clanId);

        player.sendMessage(PREFIX.append(MessageUtils.colorize(" &a✅ ¡Territorio reclamado!")));
        Bukkit.broadcast(PREFIX.append(MessageUtils.colorize(" &6🏴 " + player.getName() + " ha reclamado " + territory.getDisplayName())));
    }

    private void handleUnclaim(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Uso: /territorios unclaim <id_territorio>")));
            return;
        }

        String territoryId = args[1];
        Territory territory = plugin.getTerritoryManager().getTerritory(territoryId);

        if (territory == null) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Territorio no encontrado")));
            return;
        }

        if (!territory.isOwned()) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ Este territorio no está reclamado")));
            return;
        }

        // Verificar si es el dueño
        UUID playerClanId = getClanId(player);
        if (!territory.getOwnerClanId().equals(playerClanId)) {
            player.sendMessage(PREFIX.append(MessageUtils.colorize(" &c❌ No eres el dueño de este territorio")));
            return;
        }

        // Liberar territorio
        territory.setOwned(false);
        territory.setOwnerClanId(null);

        player.sendMessage(PREFIX.append(MessageUtils.colorize(" &a✅ ¡Territorio liberado!")));
        Bukkit.broadcast(PREFIX.append(MessageUtils.colorize(" &6⚪ " + player.getName() + " ha liberado " + territory.getDisplayName())));
    }

    private void showHelp(Player player) {
        player.sendMessage("");
        player.sendMessage(PREFIX);
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage(MessageUtils.colorize("&e📋 COMANDOS DE TERRITORIO"));
        player.sendMessage(MessageUtils.colorize("&7/territorios menu              &f- Menú de territorios"));
        player.sendMessage(MessageUtils.colorize("&7/territorios list              &f- Lista de territorios"));
        player.sendMessage(MessageUtils.colorize("&7/territorios info <id>         &f- Info del territorio"));
        player.sendMessage(MessageUtils.colorize("&7/territorios claim <id>        &f- Reclamar territorio"));
        player.sendMessage(MessageUtils.colorize("&7/territorios unclaim <id>      &f- Liberar territorio"));
        player.sendMessage(MessageUtils.colorize("&6━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        player.sendMessage("");
    }

    private boolean hasClan(Player player) {
        // TODO: Integrar con ChronicleClans
        return true;
    }

    private UUID getClanId(Player player) {
        // TODO: Integrar con ChronicleClans para obtener el UUID del clan del jugador
        // Por ahora usa el UUID del jugador como placeholder
        return player.getUniqueId();
    }
}