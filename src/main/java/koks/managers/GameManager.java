package koks.managers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import koks.SkyWars;
import koks.config.SWConfig;
import koks.objects.SWGame;
import koks.objects.SWLocation;
import koks.objects.SWTeam;

public class GameManager {

    private static SWGame game;

    public static void setupGame() {
        if (SWConfig.gameInfo.getConfig().getConfigurationSection("game") != null) {
            String name = SWConfig.gameInfo.getConfig().getString("game.world");
            SkyWars.getInstance().consoleSendMessage(name);
            game = new SWGame(name);
            FileConfiguration config = SWConfig.gameInfo.getConfig();
            if (config.getInt("game.maxPlayers") != 0)
                game.setPlayers(config.getInt("game.maxPlayers"), config.getInt("game.minPlayers"));
            try {
                String[] values = config.getString("game.lobbyPoint").split(",");
                double x = Double.parseDouble(values[0].split(":")[1]);
                double y = Double.parseDouble(values[1].split(":")[1]);
                double z = Double.parseDouble(values[2].split(":")[1]);
                game.setLobby(new Location(game.getWorld(), x, y, z));
            } catch (Exception e) {
                SkyWars.getInstance().getLogger().warning("Failed to load lobbyPoint " + config.getString("game.lobbyPoint") + ". ExceptionType: " + e);
            }
            for (String point : config.getStringList("game.spawnPoints")) {
                try {
                    String[] values = point.split(",");
                    double x = Double.parseDouble(values[0].split(":")[1]);
                    double y = Double.parseDouble(values[1].split(":")[1]);
                    double z = Double.parseDouble(values[2].split(":")[1]);
                    Location loc = new Location(game.getWorld(), x, y, z);
                    game.getTeams().add(new SWTeam(game.getTeams().size() + 1, loc));
                } catch (Exception e) {
                    SkyWars.getInstance().getLogger().warning("Failed to load spawnPoint " + point + ". ExceptionType: " + e);
                }
            }
            for (String chest : config.getStringList("game.chests.mid")) {
                try {
                    String[] values = chest.split(",");
                    int x = Integer.parseInt(values[0].split(":")[1]);
                    int y = Integer.parseInt(values[1].split(":")[1]);
                    int z = Integer.parseInt(values[2].split(":")[1]);
                    SWLocation loc = new SWLocation(x, y, z);
                    game.getChests().add(loc);
                    if (values.length > 3) loc.setData(Byte.parseByte(values[3].split(":")[1]));
                } catch (Exception e) {
                    SkyWars.getInstance().getLogger().warning("Failed to load chest location " + chest + ". ExceptionType: " + e);
                }
            }
            for (String chest : config.getStringList("game.chests.team")) {
                try {
                    String[] values = chest.split(",");
                    int x = Integer.parseInt(values[0].split(":")[1]);
                    int y = Integer.parseInt(values[1].split(":")[1]);
                    int z = Integer.parseInt(values[2].split(":")[1]);
                    SWLocation c = new SWLocation(x, y, z);
                    if (values.length > 3) c.setData(Byte.parseByte(values[3].split(":")[1]));
                    game.getTeam(new Location(game.getWorld(), x, y, z)).addChest(c);
                } catch (Exception e) {
                    SkyWars.getInstance().getLogger().warning("Failed to load chest location " + chest + ". ExceptionType: " + e);
                }
            }
        }
    }

    public static void saveGame() {
        ConfigurationSection section = SWConfig.gameInfo.createCS("game");
        if (game.getWorld() != null)
            section.set("world", game.getWorldName());
        if (game.getMaxPlayers() != 0)
            section.set("maxPlayers", game.getMaxPlayers());
        if (game.getMinPlayers() != 0)
            section.set("minPlayers", game.getMinPlayers());
        if (game.getLobby() != null)
            section.set("lobbyPoint", "X:" + game.getLobby().getX() + ", Y:" + game.getLobby().getY() + ", Z:" + game.getLobby().getZ());
        List<String> chests = new ArrayList<>();
        if (game.getChests() != null) {
            for (SWLocation chest : game.getChests()) {
                chests.add("X:" + chest.getX() + ", Y:" + chest.getY() + ", Z:" + chest.getZ() + ", Data:" + chest.getData());
            }
        }
        section.set("chests.mid", chests);
        List<String> teamSpawns = new ArrayList<>();
        List<String> teamChests = new ArrayList<>();
        for (SWTeam team : game.getTeams()) {
            if (team.getChests() != null) {
                teamSpawns.add("X:" + team.getSpawn().getX() + ", Y:" + team.getSpawn().getY() + ", Z:" + team.getSpawn().getZ());
                team.getChests().forEach(c -> teamChests.add("X:" + c.getX() + ", Y:" + c.getY() + ", Z:" + c.getZ() + ", Data:" + c.getData()));
            }
        }
        section.set("spawnPoints", teamSpawns);
        section.set("chests.team", teamChests);
        SWConfig.gameInfo.save();
    }

    public static SWGame getGame() {
        return game;
    }

    public static void setGame(SWGame g) {
        game = g;
    }

    public static void removeGame() {
        if (SWConfig.gameInfo.contains("game")) {
            SWConfig.gameInfo.getConfig().set("game", null);
            SWConfig.gameInfo.save();
        }
        game = null;
    }
    public static void createGame(String name) {
        game = new SWGame(name);
    }
}
