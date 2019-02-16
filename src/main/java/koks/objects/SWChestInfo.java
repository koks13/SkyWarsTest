package koks.objects;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import koks.managers.GameManager;

public class SWChestInfo {
	
	private ArmorStand empty;
	private ArmorStand timer;

	public SWChestInfo(Location location) {
		Location loc = location.clone();
		loc.add(0.5, -1.3, 0.5);
		timer = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		timer.setCustomName("§a");
		timer.setCustomNameVisible(true);
		timer.setGravity(false);
		timer.setVisible(false);
		for(SWPlayer player : GameManager.getGame().getPlayers()) {
			player.getScoreboard().addStand(timer.getUniqueId().toString());
		}
	}
	
	public void setEmpty(boolean emp) {
		if(emp) {
			if(empty != null) return;
			Location loc = timer.getLocation();
			loc.add(0, -0.2, 0);
			empty = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			empty.setCustomName("§c§lпустой");
			empty.setCustomNameVisible(true);
			empty.setGravity(false);
			empty.setVisible(false);
		}else {
			if(empty == null) return;
			empty.remove();
			empty = null;
		}
	}
	
	public void clear() {
		for(SWPlayer player : GameManager.getGame().getPlayers()) {
			player.getScoreboard().removeStand(timer.getUniqueId().toString());
		}
		timer.remove();
		timer = null;
		if(empty == null) return;
		empty.remove();
		empty = null;
	}
	
}
