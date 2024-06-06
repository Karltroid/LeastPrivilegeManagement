package me.karltroid.leastprivilegemanagement.commands;

import me.karltroid.leastprivilegemanagement.admins.Admin;
import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
            Player target = Bukkit.getServer().getPlayer(args[0]);
            admin.toggleAdminMode(target, null);
        }
        else if (args.length == 3)
        {
            Location tpLocation = new Location(player.getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            admin.toggleAdminMode(null, tpLocation);
        }
        else
        {
            admin.toggleAdminMode(null, null);
        }



        return true;
    }
}
