package me.karltroid.leastprivilegemanagement.commands;

import me.karltroid.leastprivilegemanagement.admins.Admin;
import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TargetCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
    {
        if (!(commandSender instanceof Player))
            return false;

        Player player = (Player) commandSender;
        if (!player.hasPermission("LeastPrivilegeManagement.admin"))
            return false;

        Admin admin = AdminManager.getOnlineAdmin(player);
        if (admin == null) return false;


        if (args.length == 1)
        {
            OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
            admin.toggleAdminMode(target, null);
        }
        else if (args.length == 3)
        {
            Location tpLocation = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            admin.toggleAdminMode(null, tpLocation);
        }
        else if (args.length == 4)
        {
            if (args[3].equals("nether") || args[3].equals("hell") || args[3].equals("world_nether")) {
                Location tpLocation = new Location(Bukkit.getWorld("world_nether"), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                admin.toggleAdminMode(null, tpLocation);
            } else if (args[3].equals("world") || args[3].equals("overworld")) {
                Location tpLocation = new Location(Bukkit.getWorld("world"), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                admin.toggleAdminMode(null, tpLocation);
            } else {
                player.sendMessage(ChatColor.RED + "Unknown world name.");
            }
        }
        else if (args.length > 4)
        {
            player.sendMessage(ChatColor.RED + "Usage: /admin <player> or /admin <x> <y> <z> <world_name(optional)>");
        }
        else
        {
            admin.toggleAdminMode(null, null);
        }

        return true;
    }
}
