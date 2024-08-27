package me.karltroid.leastprivilegemanagement;

import me.karltroid.leastprivilegemanagement.admins.AdminManager;
import me.karltroid.leastprivilegemanagement.commands.*;
import me.karltroid.leastprivilegemanagement.tools.Freeze;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class LeastPrivilegeManagement extends JavaPlugin implements Listener {

    static LeastPrivilegeManagement instance;
    AdminManager adminManager = new AdminManager();

    Freeze freeze = new Freeze();

    @Override
    public void onEnable()
    {
        instance = this;
        getServer().getPluginManager().registerEvents(adminManager, this);
        getServer().getPluginManager().registerEvents(freeze, this);
        getInstance().getCommand("target").setExecutor(new TargetCommand());
        getInstance().getCommand("reveal").setExecutor(new RevealCommand());
        getInstance().getCommand("back").setExecutor(new BackCommand());
        getInstance().getCommand("forward").setExecutor(new ForwardCommand());
        getInstance().getCommand("freeze").setExecutor(new FreezeCommand());
        getInstance().getCommand("release").setExecutor(new ReleaseCommand());
        getInstance().getCommand("yell").setExecutor(new YellCommand());
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
