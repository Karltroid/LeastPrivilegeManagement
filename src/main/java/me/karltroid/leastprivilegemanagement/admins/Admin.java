package me.karltroid.leastprivilegemanagement.admins;

import me.karltroid.leastprivilegemanagement.LeastPrivilegeManagement;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Admin
{
    Player adminPlayer;
    AdminState adminState;
    ItemStack[] savedPriorInventory;
    Location savedPriorLocation;


    public Admin(Player adminPlayer)
    {
        this.adminPlayer = adminPlayer;
        setAdminState(AdminState.FREEROAM);
    }


    /*
      * /target -> toggles on/off spectator
      * /target <player> -> spectator and tps to target
      */
    public void toggleAdminMode(Player target)
    {
        if (target != null)
        {
            List<Admin> onlineAdmins = LeastPrivilegeManagement.getInstance().getAdminManager().getOnlineAdmins();

            setAdminState(AdminState.SPECTATING);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(LeastPrivilegeManagement.getInstance(), () -> {
                adminPlayer.teleport(target); // tp to self so player goes to ground (tick later, doesn't TP otherwise)
            },  2L);

            for (Admin admin : onlineAdmins)
            {
                if (admin.adminPlayer == this.adminPlayer) continue;

                admin.adminPlayer.sendMessage(ChatColor.YELLOW + this.adminPlayer.getName() + " is spectating " + target.getName());
            }

            return;
        }

        if (adminState.equals(AdminState.FREEROAM) || adminState.equals(AdminState.REVEALED))
            setAdminState(AdminState.SPECTATING);
        else
            setAdminState(AdminState.FREEROAM);
    }


    public void setAdminState(AdminState newAdminState)
    {
        AdminState previousAdminState = this.adminState;
        this.adminState = newAdminState;
        switch (newAdminState)
        {
            case FREEROAM:
                if (previousAdminState == AdminState.FREEROAM) break;
                adminPlayer.setGameMode(GameMode.SURVIVAL);
                loadPriorData();
                break;


            case SPECTATING:
                if (previousAdminState.equals(AdminState.FREEROAM)) savePriorData();
                adminPlayer.setGameMode(GameMode.SPECTATOR);
                break;


            case REVEALED:
                if (!previousAdminState.equals(AdminState.SPECTATING)) break;

                Location adminLocation = adminPlayer.getLocation();
                for (int y = 0; y < 100; y++)
                {
                    // teleport admin to first valid block location below them
                    Location adminTpLocation = adminLocation.clone().subtract(0, y, 0);
                    if (adminTpLocation.getBlock().getType() == Material.AIR) continue;

                    adminPlayer.teleport(adminTpLocation.add(0, 1, 0));
                    break;
                }

                // reveal the admin player by setting their gamemode to survival
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(LeastPrivilegeManagement.getInstance(), () -> {
                    adminPlayer.setGameMode(GameMode.SURVIVAL);
                },  1L);

                break;

            default:
                break;
        }
    }

    public AdminState getAdminState()
    {
        return adminState;
    }


    void savePriorData()
    {
        savedPriorLocation = adminPlayer.getLocation();
        savedPriorInventory = adminPlayer.getInventory().getContents();
        adminPlayer.getInventory().clear();
    }


    void loadPriorData()
    {
        if (savedPriorLocation != null) adminPlayer.teleport(savedPriorLocation);
        savedPriorLocation = null;

        if (savedPriorInventory != null) adminPlayer.getInventory().setContents(savedPriorInventory);
        savedPriorInventory = null;
    }
}
