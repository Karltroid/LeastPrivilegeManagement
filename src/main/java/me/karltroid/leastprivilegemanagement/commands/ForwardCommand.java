package me.karltroid.leastprivilegemanagement.commands;

import me.karltroid.leastprivilegemanagement.LeastPrivilegeManagement;
import me.karltroid.leastprivilegemanagement.admins.Admin;
import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForwardCommand implements CommandExecutor
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

        admin.teleportForwardInHistory();
        return true;
    }
}
