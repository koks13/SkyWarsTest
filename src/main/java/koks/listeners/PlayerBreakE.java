package koks.listeners;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import koks.managers.GameManager;
import org.bukkit.event.entity.EntityExplodeEvent;

public class PlayerBreakE implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(e.getPlayer().hasPermission("sw.build")) return;
        if (e.getBlock().getState() instanceof Chest && GameManager.getGame() != null) {
            if (GameManager.getGame().getChestsInfo().containsKey(e.getBlock().getLocation())) {
                GameManager.getGame().getChestsInfo().get(e.getBlock().getLocation()).clear();
                GameManager.getGame().getChestsInfo().remove(e.getBlock().getLocation());
            }
            e.setDropItems(false);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        e.blockList().forEach(block -> {
            if (block.getState() instanceof Chest && GameManager.getGame() != null) {
                if (GameManager.getGame().getChestsInfo().containsKey(block.getLocation())) {
                    GameManager.getGame().getChestsInfo().get(block.getLocation()).clear();
                    GameManager.getGame().getChestsInfo().remove(block.getLocation());
                }
            }
        });
    }
}


