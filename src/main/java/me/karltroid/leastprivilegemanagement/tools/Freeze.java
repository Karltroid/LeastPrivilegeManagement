package me.karltroid.leastprivilegemanagement.tools;

import me.karltroid.leastprivilegemanagement.admins.Admin;
import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Freeze implements Listener {
    static List<UUID> frozenPlayers = new ArrayList<>();

    public static void freezePlayer(Player freezer, OfflinePlayer target) {
        frozenPlayers.add(target.getUniqueId());

        String freezerName = freezer == null ? "CONSOLE" : freezer.getName();

        if (target.isOnline())
            ((Player) target).sendMessage(ChatColor.RED + freezerName + " has frozen you!");

        List<Admin> onlineAdmins = AdminManager.getOnlineAdmins();
        for (Admin admin : onlineAdmins) {
            admin.getPlayer().sendMessage(ChatColor.GOLD + freezerName + " has frozen " + target.getName() + "!");
        }
    }

    public static void releasePlayer(Player freezer, OfflinePlayer target) {
        frozenPlayers.remove(target.getUniqueId());

        String freezerName = freezer == null ? "CONSOLE" : freezer.getName();

        if (target.isOnline())
            ((Player) target).sendMessage(ChatColor.RED + freezerName + " has released you!");

        List<Admin> onlineAdmins = AdminManager.getOnlineAdmins();
        for (Admin admin : onlineAdmins) {
            admin.getPlayer().sendMessage(ChatColor.GOLD + freezerName + " has released " + target.getName() + "!");
        }
    }

    public static List<UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    @EventHandler
    public void onFrozenPlayerMoving(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!frozenPlayers.contains(player.getUniqueId()))
            return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            from.setDirection(to.getDirection());
            event.setTo(from);
        }
    }
}
