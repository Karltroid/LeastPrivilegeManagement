package me.karltroid.leastprivilegemanagement.commands;

import me.karltroid.leastprivilegemanagement.admins.Admin;
import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import me.karltroid.leastprivilegemanagement.admins.AdminState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RevealCommand implements CommandExecutor
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

        switch (admin.getAdminState())
        {
            case REVEALED:
                player.sendMessage("You are already revealed.");
                return false;
            case FREEROAM:
                player.sendMessage("You can only reveal if in /admin mode.");
                return false;
            default:
                admin.setAdminState(AdminState.REVEALED);
                return true;
        }
    }
}
