package koks.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import koks.SkyWars;

public class WorldManager {
	
	public static void deleteWorld(World world) {
		if(world != null) {
			for (Player player : world.getPlayers()) {
				player.teleport(Bukkit.getWorld("world").getSpawnLocation());
			}

			SkyWars.getInstance().getServer().unloadWorld(world, false);
			String name = world.getName();
			String rootDirectory = SkyWars.getInstance().getServer().getWorldContainer().getAbsolutePath();
			File destFolder = new File(rootDirectory + "/" + name);
			delete(destFolder);
		}
	}
	
    private static void delete(File delete){
        if (delete.isDirectory()) {
            String[] files = delete.list();
            
            if (files != null) {
                for (String file : files) {
                    File toDelete = new File(file);
                    delete(toDelete);
                }
            }
        } else delete.delete();  
    }

	public static void resetWorld(World world) {
		if(world != null) {
			for (Player player : world.getPlayers()) {
				if(Bukkit.getWorlds().get(0) != world) player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
				else player.kickPlayer("Мир перезагружается!");
			}
			SkyWars.getInstance().getServer().unloadWorld(world, false);
			copyWorld(world.getName().split("_")[0]);
		}
	}

    public static void copyWorld(String worldName) {
        String rootDirectory = SkyWars.getInstance().getServer().getWorldContainer().getAbsolutePath();      
        
        File srcFolder = new File(rootDirectory + "/" + worldName);
        File destFolder = new File(rootDirectory + "/" + worldName + "_active");
        
        delete(destFolder);
        try {
            copyFolder(srcFolder, destFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {

            if(!dest.exists()) dest.mkdir();

            String files[] = src.list();

            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(src, file);
                    File destFile = new File(dest, file);
                    copyFolder(srcFile, destFile);
                }
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
    
    public static boolean containsWorld(String worldName) {
    	File files = new File(SkyWars.getInstance().getServer().getWorldContainer().getAbsolutePath());
    	for(File file : files.listFiles()) {
    		if(file.isDirectory()){
    			if(worldName.equalsIgnoreCase(file.getName())) return true;
    		}
    	}
    	return false;
    }
    
    public static List<String> getMaps() {
    	List<String> worlds = new ArrayList<>();
    	File files = new File(SkyWars.getInstance().getServer().getWorldContainer().getAbsolutePath());
    	for(File file : files.listFiles()) {
    		if(file.isDirectory()){
    			for(String fileName : file.list()) {
    				if(fileName.equalsIgnoreCase("DIM1")) worlds.add(file.getName());
    			}
    		}
    	}
    	return worlds;
    }
}
