package me.karltroid.leastprivilegemanagement.commands;

import me.karltroid.leastprivilegemanagement.admins.Admin;
import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import me.karltroid.leastprivilegemanagement.tools.Freeze;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "You did not provide a player to Freeze. /Freeze <player>");
            return false;
        }

        Player freezer = null;
        if (commandSender instanceof Player) {
            freezer = (Player) commandSender;
            if (!freezer.hasPermission("LeastPrivilegeManagement.admin")) {
                freezer.sendMessage(ChatColor.RED + "You do not have permission to use this command");
                return false;
            }
        }

        OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
        if (target.getName() == null) {
            commandSender.sendMessage(ChatColor.RED + "Invalid player name, they have never played before.");
            return false;
        }

        if (Freeze.getFrozenPlayers().contains(target.getUniqueId())) {
            commandSender.sendMessage(ChatColor.RED + "This player is already frozen!");
            return false;
        }

        Freeze.freezePlayer(freezer, target);
        return true;
    }
}
