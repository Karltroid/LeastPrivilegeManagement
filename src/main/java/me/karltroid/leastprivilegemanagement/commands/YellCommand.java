package me.karltroid.leastprivilegemanagement.commands;

import me.karltroid.leastprivilegemanagement.tools.Freeze;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class YellCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (args.length < 2) {
            commandSender.sendMessage(ChatColor.RED + "/yell <player> <msg>");
            return false;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            commandSender.sendMessage(ChatColor.RED + args[0] + " is not online.");
            return false;
        }

        // Combine all args after <player> into a single message
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        target.sendTitle(ChatColor.RED + message, "", 10, 160, 10);
        return true;
    }
}
