package koks.listeners;

import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import koks.SkyWars;
import koks.objects.SWPlayer;
import koks.objects.SWGame.GameStatus;

public class MoveE implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(e.getPlayer().hasPermission("sw.build")) return;
        SWPlayer player = SkyWars.getInstance().getPlayer(e.getPlayer());
        if (player == null) return;
        if (!player.isGame() || player.getGame().isStatus(GameStatus.RELOAD)) return;
        if (player.isSpectator()) {
            if (player.getPlayer().getLocation().distance(player.getGame().getLobby()) >= 150) {
                player.teleport(player.getGame().getLobby());
                return;
            }
        }
        if (player.getPlayer().getLocation().getY() <= -5) {
            if (player.getGame().isStatus(GameStatus.LOBBY)) {
                e.getPlayer().teleport(player.getGame().getLobby());
                return;
            }
            player.getPlayer().setHealth(0.0d);
        }
    }

    @EventHandler
    public void onEnderDragonChangePhaseEvent(EnderDragonChangePhaseEvent e) {
        if (e.getNewPhase() == Phase.FLY_TO_PORTAL || e.getNewPhase() == Phase.LAND_ON_PORTAL || e.getNewPhase() == Phase.BREATH_ATTACK || e.getNewPhase() == Phase.STRAFING)
            e.setCancelled(true);
    }
}
