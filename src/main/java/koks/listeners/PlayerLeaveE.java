package koks.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import koks.SkyWars;
import koks.objects.SWPlayer;

public class PlayerLeaveE implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player p = event.getPlayer();
        SWPlayer player = SkyWars.getInstance().getPlayer(p);
        if (player != null && player.isGame()) {
            player.savePlayerStats();
            player.getGame().leave(player);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.getInventory().clear();
        }
        p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
    }
}
