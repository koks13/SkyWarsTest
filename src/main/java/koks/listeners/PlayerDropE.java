package koks.listeners;

import koks.SkyWars;
import koks.objects.SWGame;
import koks.objects.SWPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropE implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if(e.getPlayer().hasPermission("sw.build")) return;
        SWPlayer player = SkyWars.getInstance().getPlayer(e.getPlayer());

        if (player == null) return;

        if (!player.isGame()) {
            e.setCancelled(true);
            return;
        }

        if (player.getGame().isStatus(SWGame.GameStatus.LOBBY) || player.getGame().isStatus(SWGame.GameStatus.STARTING)) {
            e.setCancelled(true);
            return;
        }
    }
}
