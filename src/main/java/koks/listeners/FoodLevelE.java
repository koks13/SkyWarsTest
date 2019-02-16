package koks.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import koks.SkyWars;
import koks.objects.SWGame.GameStatus;
import koks.objects.SWPlayer;

public class FoodLevelE implements Listener{
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if(e.getEntity() instanceof Player) {
			SWPlayer player = SkyWars.getInstance().getPlayer((Player) e.getEntity());
			if(player != null && player.isGame()) {
				if(player.getGame().isStatus(GameStatus.LOBBY) || player.getGame().isStatus(GameStatus.STARTING)) e.setCancelled(true);
			} else e.setCancelled(true);
		}
	}
}
