package koks.listeners;

import koks.SkyWars;
import koks.objects.SWGame;
import koks.objects.SWPlayer;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceE implements Listener{
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().hasPermission("sw.build")) return;
		SWPlayer player = SkyWars.getInstance().getPlayer(e.getPlayer());

		if (player == null || !player.isGame()) return;

		if (player.getGame().isStatus(SWGame.GameStatus.LOBBY) || player.getGame().isStatus(SWGame.GameStatus.STARTING)) {
			e.setCancelled(true);
			return;
		}

		if(e.getBlockPlaced().getState() instanceof Chest){
			e.setCancelled(true);
			player.sendMessage("&cВы не можете поставить этот блок!!!");
		}
	}
}
