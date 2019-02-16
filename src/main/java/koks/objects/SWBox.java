package koks.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class SWBox {
	
    private Location base;
    private Material boxMaterial;
    private int x;
    private int y;
    private int z;
    
    public SWBox(Location base) {
        this.base = base;
        boxMaterial = Material.GLASS;
        this.x = this.base.getBlockX();
        this.y = this.base.getBlockY();
        this.z = this.base.getBlockZ();
    }
    
    public void setBox() {
    	World world = base.getWorld();
    	
        world.getBlockAt(this.x, this.y - 1, this.z).setType(boxMaterial);
        
        world.getBlockAt(this.x, this.y, this.z - 1).setType(boxMaterial);
        world.getBlockAt(this.x, this.y + 1, this.z - 1).setType(boxMaterial);
        world.getBlockAt(this.x, this.y + 2, this.z - 1).setType(boxMaterial);
        
        world.getBlockAt(this.x, this.y, this.z + 1).setType(boxMaterial);
        world.getBlockAt(this.x, this.y + 1, this.z + 1).setType(boxMaterial);
        world.getBlockAt(this.x, this.y + 2, this.z + 1).setType(boxMaterial);
        
        world.getBlockAt(this.x + 1, this.y, this.z).setType(boxMaterial);
        world.getBlockAt(this.x + 1, this.y + 1, this.z).setType(boxMaterial);
        world.getBlockAt(this.x + 1, this.y + 2, this.z).setType(boxMaterial);
        
        world.getBlockAt(this.x - 1, this.y, this.z).setType(boxMaterial);
        world.getBlockAt(this.x - 1, this.y + 1, this.z).setType(boxMaterial);
        world.getBlockAt(this.x - 1, this.y + 2, this.z).setType(boxMaterial);
        
        world.getBlockAt(this.x, this.y + 3, this.z).setType(boxMaterial); 
    }
    
    public Location getLocation() {
        return this.base;
    }

	public void removeBox() {
		boxMaterial = Material.AIR;
		setBox();
	}
}
