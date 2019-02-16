package koks.listeners;

import koks.managers.CoinsManager;
import koks.managers.ExperienceManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import koks.SkyWars;
import koks.objects.SWPlayer;
import koks.objects.SWGame.GameStatus;


public class PlayerDeathE implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        SWPlayer player = SkyWars.getInstance().getPlayer(e.getEntity());
        if (player == null) return;
        if (!player.isGame()) return;
        player.setSpectator(true);
        player.getStats().addDeaths();
        CoinsManager.coinsAdder(player, CoinsManager.Cause.DEATH);
        ExperienceManager.experienceAdder(player, ExperienceManager.Cause.DEATH);
        if (e.getEntity().getKiller() instanceof Player) {
            SWPlayer killer = SkyWars.getInstance().getPlayer(e.getEntity().getKiller());
            if (killer == null) return;
            if (!killer.isGame()) return;
            killer.addKill();
            killer.addSouls(1);
            killer.sendMessage("&b+1 душа!");
            CoinsManager.coinsAdder(killer, CoinsManager.Cause.KILL);
            ExperienceManager.experienceAdder(killer, ExperienceManager.Cause.KILL);
            killer.getGame().sendMessage("&b" + killer.getName() + " &eубил &b" + player.getName());
        } else player.getGame().sendMessage("&b" + player.getName() + " &eумер");
        player.death();
        player.getGame().winnerCheck();
        new BukkitRunnable() {
            public void run() {
                player.getPlayer().spigot().respawn();
            }
        }.runTaskLater(SkyWars.getInstance(), 10L);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        SWPlayer player = SkyWars.getInstance().getPlayer(e.getPlayer());
        if (player == null) {
            e.setRespawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
            return;
        }
        if (!player.isGame()) {
            e.setRespawnLocation(Bukkit.getWorlds().get(0).getSpawnLocation());
            return;
        }
        if (player.getGame().isStatus(GameStatus.LOBBY)) {
            player.teleport(player.getGame().getLobby());
            return;
        }
        e.setRespawnLocation(player.getGame().getLobby());
        player.getPlayer().setGameMode(GameMode.SPECTATOR);
    }


}
