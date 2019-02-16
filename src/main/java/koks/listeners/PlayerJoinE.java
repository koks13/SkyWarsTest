package koks.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import koks.SkyWars;
import koks.managers.GameManager;
import koks.objects.SWPlayer;

public class PlayerJoinE implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        e.setJoinMessage(null);
        if (p.hasPermission("sw.build")) p.addAttachment(SkyWars.getInstance(), "sw.build", false);
        if (GameManager.getGame() != null) {
            SWPlayer player = new SWPlayer(p);
            SkyWars.getInstance().addPlayer(player);
            GameManager.getGame().join(player);
        }
    }
}
