package koks.listeners;

import koks.SkyWars;
import koks.managers.KitManager;
import koks.objects.SWKit;
import koks.objects.SWPlayer;
import koks.utility.ChatUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickE implements Listener{
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equalsIgnoreCase(ChatUtil.format("&b&lКИТЫ"))) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR && KitManager.getKits().containsKey(e.getCurrentItem().getItemMeta().getDisplayName())) {
				SWPlayer player = SkyWars.getInstance().getPlayer((Player) e.getWhoClicked());
				player.selectKit(KitManager.getKits().get(e.getCurrentItem().getItemMeta().getDisplayName()));
				player.getPlayer().playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BELL, 1, 1);
			}
		}else if(e.getInventory().getTitle().equalsIgnoreCase(ChatUtil.format("&b&lМАГАЗИН КИТОВ"))){
			e.setCancelled(true);
			if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR &&KitManager.getKits().containsKey(e.getCurrentItem().getItemMeta().getDisplayName())) {
				SWPlayer player = SkyWars.getInstance().getPlayer((Player) e.getWhoClicked());
				if(player != null){
					SWKit kit = KitManager.getKits().get(e.getCurrentItem().getItemMeta().getDisplayName());
					if(!player.getStats().containsKit(kit)) {
						if (player.getStats().getCoins() >= kit.getCost()) {
							player.getStats().addCoins(-kit.getCost());
							player.getStats().addKits(kit);
							player.sendMessage("&aкит " + kit.getKitName() + " &aуспешно приобретён!");
							player.getPlayer().playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BELL, 1, 1);
							player.getPlayer().openInventory(KitManager.getKitsShop(player));
						} else {
							player.sendMessage("&cУ вас недостаточно коинов для покупки этого кита!");
							player.getPlayer().playSound(e.getWhoClicked().getLocation(), Sound.BLOCK_NOTE_BASS, 1, 1);
						}
					}
				}
			}
		}
	}
}
