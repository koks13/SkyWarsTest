package koks.listeners;

import koks.managers.GameManager;
import koks.managers.KitManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import koks.SkyWars;
import koks.objects.SWGame;
import koks.objects.SWPlayer;
import koks.objects.SWGame.GameStatus;
import koks.objects.SWLocation;

public class PlayerInteractE implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getPlayer().hasPermission("sw.build")) return;
        SWPlayer player = SkyWars.getInstance().getPlayer(e.getPlayer());
        if (player != null && player.isGame()) {
            if (player.getGame().isStatus(GameStatus.LOBBY) || player.getGame().isStatus(GameStatus.RELOAD))
                e.setCancelled(true);
        }else return;
        if (player.getGame().isStatus(GameStatus.LOBBY) || player.getGame().isStatus(GameStatus.STARTING)) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BOW)
                player.openInventoryByKits();
            if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EMERALD)
                e.getPlayer().openInventory(KitManager.getKitsShop(player));
        }
        if (!e.getPlayer().isOp() && e.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getState() instanceof Chest) {
            addChest(e);
        }
    }

    public void addChest(PlayerInteractEvent e) {
        SWGame game = GameManager.getGame();
        if (game == null) return;
        Chest c = (Chest) e.getClickedBlock().getState();
        SWLocation chest = new SWLocation(c.getX(), c.getY(), c.getZ(), c.getRawData());
        if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.EMERALD) {
            e.setCancelled(true);
            if (game.containsChest(chest)) {
                e.getPlayer().sendMessage("уже есть!");
            } else {
                game.getChests().add(chest);
                e.getPlayer().sendMessage("добавлен!");
            }
        } else if (e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND) {
            e.setCancelled(true);
            if (game.getTeam(c.getLocation()).addChest(chest)) {
                e.getPlayer().sendMessage("добавлен!" + "[" + game.getTeam(c.getLocation()).getID() + "]");
            } else {
                e.getPlayer().sendMessage("уже есть!");
            }
        }
    }

    @EventHandler
    public void onArmorStandManipulation(PlayerArmorStandManipulateEvent e) {
        SWPlayer player = SkyWars.getInstance().getPlayer(e.getPlayer());
        if (player != null && player.isGame()) {
            e.setCancelled(true);
        }
    }
}


