package me.karltroid.leastprivilegemanagement;

import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import me.karltroid.leastprivilegemanagement.commands.RevealCommand;
import me.karltroid.leastprivilegemanagement.commands.TargetCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LeastPrivilegeManagement extends JavaPlugin implements Listener {

    static LeastPrivilegeManagement instance;
    AdminManager adminManager = new AdminManager();

    @Override
    public void onEnable()
    {
        instance = this;
        getServer().getPluginManager().registerEvents(adminManager, this);
        getInstance().getCommand("target").setExecutor(new TargetCommand());
        getInstance().getCommand("reveal").setExecutor(new RevealCommand());
    }

    @Override
    public void onDisable()
    {
        adminManager.clearAdmins();
    }

    public static LeastPrivilegeManagement getInstance()
    {
        return instance;
    }

    public AdminManager getAdminManager() {
        return adminManager;
    }
}
