package me.karltroid.leastprivilegemanagement.commands;

import me.karltroid.leastprivilegemanagement.tools.Freeze;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReleaseCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "You did not provide a player to Freeze. /release <player>");
            return false;
        }

        Player releaser = null;
        if (commandSender instanceof Player) {
            releaser = (Player) commandSender;
            if (!releaser.hasPermission("LeastPrivilegeManagement.admin")) {
                releaser.sendMessage(ChatColor.RED + "You do not have permission to use this command");
                return false;
            }
        }

        OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
        if (target.getName() == null) {
            commandSender.sendMessage(ChatColor.RED + "Invalid player name, they have never played before.");
            return false;
        }

        if (!Freeze.getFrozenPlayers().contains(target.getUniqueId())) {
            commandSender.sendMessage(ChatColor.RED + "This player is not frozen, no need to release them.");
            return false;
        }

        Freeze.releasePlayer(releaser, target);
        return true;
    }
}
