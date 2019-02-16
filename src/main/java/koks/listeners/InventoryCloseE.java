package koks.listeners;

import koks.objects.SWGame;
import koks.utility.ChatUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import koks.managers.GameManager;
import koks.objects.SWChestInfo;

public class InventoryCloseE implements Listener {
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(e.getPlayer().hasPermission("sw.build")) return;
		if(e.getInventory().getType() == (InventoryType.CHEST)) {
			if(GameManager.getGame().isStatus(SWGame.GameStatus.LOBBY) || GameManager.getGame().isStatus(SWGame.GameStatus.STARTING) || GameManager.getGame().isStatus(SWGame.GameStatus.RELOAD)) return;
			if(e.getInventory().getTitle().equalsIgnoreCase(ChatUtil.format("&b&lКИТЫ")) || e.getInventory().getTitle().equalsIgnoreCase(ChatUtil.format("&b&lМАГАЗИН КИТОВ"))) return;
			Location loc = e.getInventory().getLocation();
			if(!GameManager.getGame().getChestsInfo().keySet().contains(loc)) {
				GameManager.getGame().getChestsInfo().put(loc, new SWChestInfo(loc));
			}
			for(int i = 0; i < e.getInventory().getSize()-1; i++) {
				if(e.getInventory().getItem(i) != null) {
					GameManager.getGame().getChestsInfo().get(loc).setEmpty(false);
					return;
				}
			}
			GameManager.getGame().getChestsInfo().get(loc).setEmpty(true);
		}
	}
}
