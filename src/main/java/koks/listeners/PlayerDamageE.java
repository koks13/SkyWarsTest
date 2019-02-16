package koks.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import koks.SkyWars;
import koks.objects.SWPlayer;
import koks.objects.SWGame.GameStatus;

public class PlayerDamageE implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            SWPlayer player = SkyWars.getInstance().getPlayer((Player) e.getEntity());
            if (player == null || !player.isGame()) {
                e.setCancelled(true);
                return;
            }
            if (player.getGame().isStatus(GameStatus.LOBBY) || player.getGame().isStatus(GameStatus.STARTING)) {
                e.setCancelled(true);
                return;
            }
            if (player.getPlayer().getNoDamageTicks() > 0 && e.getCause() == DamageCause.FALL)
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            SWPlayer player = SkyWars.getInstance().getPlayer((Player) e.getEntity());
            SWPlayer damager = SkyWars.getInstance().getPlayer((Player) e.getDamager());
            if (player == null || damager == null) {
                e.setCancelled(true);
                return;
            }
            if (!player.isGame() || !damager.isGame()) {
                e.setCancelled(true);
                return;
            }
            if (player.getGame().isStatus(GameStatus.LOBBY) || player.getGame().isStatus(GameStatus.STARTING) || player.getGame().isStatus(GameStatus.ENDING)) {
                e.setCancelled(true);
                return;
            }
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            if (((Arrow) e.getDamager()).getShooter() instanceof Player) {
                SWPlayer damager = SkyWars.getInstance().getPlayer((Player) ((Arrow) e.getDamager()).getShooter());
                if (damager != null && damager.isGame()) {
                    Player player = (Player) e.getEntity();
                    double health = (player.getHealth()-e.getFinalDamage()) > 0 ? (player.getHealth()-e.getFinalDamage()) : 0;
                    damager.sendMessage( "&b&lУ &e" + player.getName() + " &b&lосталось &c" + health + "❤");
                }
            }
        }
    }
}
