package koks.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class SWTeam {
	
	private int id;
	private ArrayList<SWPlayer> players;
	private SWPlayer player;
	private Location spawn;
	private SWBox box;
	private List<SWLocation> chests;
	
	public SWTeam(int id, Location loc) {
		this.id = id;
		setSpawn(loc);
		setBox();
		this.chests = new ArrayList<>(3);
	}
	
	public ArrayList<SWPlayer> getPlayers() {
		return players;
	}
	
	public SWPlayer getPlayer() {
		return player;
	}
	
	public int getID() {
		return id;
	}
	
	public void teleport() {
		player.teleport(spawn);
	}
	
	public boolean addPlayer(SWPlayer p) {
		if(player != null) return false;
		player = p;
		return true;
	}
	
	public boolean removePlayer(SWPlayer p) {
		if(player == null) return false;
		if(!p.getName().equalsIgnoreCase(player.getName())) return false;
		player = null;
		return true;
	}
	
	public boolean isLiving() {
		if(player == null) return false;
		return true;
	}
	
	public Location getSpawn() {
		return spawn;
	}
	
	public void setSpawn(Location spawn) {
		this.spawn = spawn.getBlock().getLocation().add(0.5, 0, 0.5);
		this.spawn.setPitch(2);
		this.spawn.setYaw(spawn.getYaw());
		setBox();
	}
	
	public List<SWLocation> getChests(){
		return chests;
	}
	
	public void clear() {
		this.players = null;
		this.player = null;
	}
	
	public boolean addChest(SWLocation location) {
		if(containsChest(location)) return false;
		chests.add(location);
		return true;
	}
	
	public boolean removeChest(SWLocation location) {
		if(!containsChest(location)) return false;
		chests.remove(location);
		return true;
	}
	
	public boolean containsChest(SWLocation location) {
		for(SWLocation loc : chests) {
			if(loc.getX() == location.getX() && loc.getY() == location.getY() && loc.getZ() == location.getZ()) {
				return true;
			}
		}
		return false;
	}

	public void setBox() {
		if(box != null) removeBox();
		this.box = new SWBox(spawn);
		box.setBox();
	}
	
	public void removeBox() {
		box.removeBox();

	}
}
