package koks.objects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import koks.PacketHandler;
import koks.PluginMessage;
import koks.managers.*;
import net.minecraft.server.v1_12_R1.EntityEnderDragon;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Chest;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.EnderDragon.Phase;

import koks.utility.ItemBuilder;
import koks.SkyWars;
import koks.timers.GameTimer;
import koks.timers.ReloadTimer;
import koks.timers.StartingGameTimer;
import koks.timers.Timer;
import koks.utility.ChatUtil;
import koks.utility.NMS;
import koks.utility.NMS.TitleAction;

public class SWGame {

    private String worldName;
    private int maxPlayers;
    private int minPlayers;
    private World world;
    private Location gameLobbyPoint;

    private Map<String, Integer> topKills = new HashMap<>();
    private Set<SWPlayer> players;
    private List<SWTeam> teams;
    private List<SWTeam> livingTeams;
    private List<SWLocation> chests;
    private Map<Location, SWChestInfo> chestsInfo;
    private GameStatus status;
    public Timer timer;

    public SWGame(String worldName) {
        this.minPlayers = 2;
        this.maxPlayers = 8;
        this.worldName = worldName;
        if (WorldManager.containsWorld(worldName)) {
            WorldManager.copyWorld(worldName);
            this.world = Bukkit.createWorld(new WorldCreator(worldName + "_active"));
            this.world.setGameRuleValue("doDaylightCycle", "false");
            this.world.setGameRuleValue("doWeatherCycle", "false");
        }
        this.teams = new ArrayList<>();
        this.players = new HashSet<>();
        this.status = GameStatus.LOBBY;
        this.livingTeams = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.chestsInfo = new HashMap<>();
        GameManager.setGame(this);
    }

    public boolean join(SWPlayer player) {
        Player p = player.getPlayer();
        p.getInventory().clear();
        p.setHealth(20);
        p.setFoodLevel(20);
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
        getPlayers().add(player);
        player.setGame(this);
        player.createScoreboard();
        if (isStatus(GameStatus.LOBBY) || isStatus(GameStatus.STARTING)) {
            if (getPlayers().size() == getMaxPlayers()) {
                player.sendMessage("&cИгра заполнена!!");
                p.setGameMode(GameMode.SPECTATOR);
                player.setSpectator(true);
                player.teleport(gameLobbyPoint);
                return false;
            }
            for (SWTeam team : getTeams()) {
                if (team.addPlayer(player)) {
                    if (isStatus(GameStatus.STARTING)) player.teleport(team.getSpawn());
                    else player.teleport(gameLobbyPoint);
                    livingTeams.add(team);
                    break;
                }
            }
            player.getStats().addKits(KitManager.getDefaultKit());
            player.selectKit(KitManager.getDefaultKit());
            if (SkyWars.getInstance().getMongoDb() != null) {
                SkyWars.getInstance().getMongoDb().loadPlayerStats(player);
            }
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().setItem(0, new ItemBuilder.Builder(Material.BOW).setDisplayName("&b&lКИТЫ").build().getItem());
            p.getInventory().setItem(1, new ItemBuilder.Builder(Material.EMERALD).setDisplayName("&a&lМАГАЗИН КИТОВ").build().getItem());
            sendMessage("&a" + player.getPlayer().getName() + " &eприсоединился!");
            playerCheck();
        } else {
            p.setGameMode(GameMode.SPECTATOR);
            player.teleport(gameLobbyPoint);
        }
        return true;
    }

    public void leave(SWPlayer player) {
        for (SWTeam team : getTeams()) {
            if (team.removePlayer(player)) {
                livingTeams.remove(team);
                break;
            }
        }
        Player p = player.getPlayer();
        p.getInventory().clear();
        p.setHealth(20);
        p.setFoodLevel(20);
        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
        getPlayers().remove(player);
        player.removeScoreboard();
        SkyWars.getInstance().removePlayer(player.getUniqueId());
        sendMessage("&6" + player.getPlayer().getName() + " &eпокинул игру!");
        if (isStatus(GameStatus.LOBBY)) playerCheck();
    }

    public boolean playerCheck() {
        if (isStatus(GameStatus.LOBBY)) {
            players.forEach(player -> {
                if (player.getScoreboard() != null) player.getScoreboard().updateLobbyScoreboard();
            });
            if (players.size() >= getMinPlayers()) {
                setStatus(GameStatus.STARTING);
                timer = new StartingGameTimer(GameStatus.STARTING.getTime(), this);
                livingTeams.forEach(team -> team.teleport());
                return true;
            }
        } else if (isStatus(GameStatus.STARTING)) {
            if (players.size() < getMinPlayers()) {
                setStatus(GameStatus.LOBBY);
                players.forEach(player -> {
                    player.teleport(gameLobbyPoint);
                    if (player.getScoreboard() != null) player.getScoreboard().updateLobbyScoreboard();
                });
                return false;
            }
        }
        return true;
    }

    public void startGame() {
        setStatus(GameStatus.REFILL_CHESTS_1);
        fillChests();
        for (SWTeam team : livingTeams) {
            SWPlayer player = team.getPlayer();
            player.getStats().addGames();
            Player p = player.getPlayer();
            NMS.sendTitle(p, "&6&lИгра началась!", TitleAction.TITLE);
            NMS.sendTitle(p, " ", TitleAction.SUBTITLE);
            p.closeInventory();
            p.setNoDamageTicks(40);
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();
            player.getScoreboard().createGameScoreboard();
            player.giveKitItems();
            addTopKilss(p.getName(), 0);
            team.removeBox();
        }
        setTimer(new GameTimer(status.getTime(), this));
    }

    public boolean winnerCheck() {
        if (isStatus(GameStatus.RELOAD)) return false;
        if (getLivingTeams().size() > 1) return false;

        timer.cancel();
        getPlayers().forEach(p -> p.removeScoreboard());
        setStatus(GameStatus.RELOAD);

        List<String> top = new ArrayList<>();
        topKills.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3).forEach(name -> top.add(name.getKey() + " &r[&e" + name.getValue() + "&r]"));

        if(getLivingTeams().size() == 1) {
            SWPlayer playerWin = getLivingTeams().get(0).getPlayer();

            NMS.sendTitle(playerWin.getPlayer(), "&e&lПОБЕДА", TitleAction.TITLE);
            playerWin.getStats().addWins();
            CoinsManager.coinsAdder(playerWin, CoinsManager.Cause.WIN);
            ExperienceManager.experienceAdder(playerWin, ExperienceManager.Cause.WIN);

            for (SWPlayer player : getPlayers()) {
                player.sendMessage(ChatUtil.format("&a============================="));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("   &lПобедил &7>> &b" + playerWin.getName()));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("   &e&lТоп по убийствам "));
                player.sendMessage(ChatUtil.format("   1 &7>> &b" + top.get(0)));
                if (top.size() > 1) player.sendMessage(ChatUtil.format("   2 &7>> &b" + top.get(1)));
                if (top.size() > 2) player.sendMessage(ChatUtil.format("   3 &7>> &b" + top.get(2)));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("   &e&lВаша статистика за эту игру "));
                player.sendMessage(ChatUtil.format("   &lПолучено коинов &7>> &e" + player.getCoins()));
                player.sendMessage(ChatUtil.format("   &lПолучено душ &7>> &b" + player.getSouls()));
                player.sendMessage(ChatUtil.format("   &lПолучено опыта &7>> &5" + player.getExp()));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("&a============================="));
                player.savePlayerStats();
            }
            setTimer(new ReloadTimer(GameStatus.RELOAD.getTime(), this));
            return true;
        }else {
            for (SWPlayer player : getPlayers()) {
                player.sendMessage(ChatUtil.format("&a============================="));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("   &lНичья"));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("   &e&lТоп по убийствам "));
                player.sendMessage(ChatUtil.format("   1 &7>> &b" + top.get(0)));
                if (top.size() > 1) player.sendMessage(ChatUtil.format("   2 &7>> &b" + top.get(1)));
                if (top.size() > 2) player.sendMessage(ChatUtil.format("   3 &7>> &b" + top.get(2)));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("   &e&lВаша статистика за эту игру "));
                player.sendMessage(ChatUtil.format("   &lПолучено коинов &7>> &e" + player.getCoins()));
                player.sendMessage(ChatUtil.format("   &lПолучено душ &7>> &b" + player.getSouls()));
                player.sendMessage(ChatUtil.format("   &lПолучено опыта &7>> &5" + player.getExp()));
                player.sendMessage(" ");
                player.sendMessage(ChatUtil.format("&a============================="));
                player.savePlayerStats();
            }
            setTimer(new ReloadTimer(GameStatus.RELOAD.getTime(), this));
            return true;
        }
    }

    public void reload() {
        for (SWPlayer player : players) {
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
            if(SkyWars.getInstance().isBungee()) SkyWars.getInstance().pluginMessage.connect(player.getPlayer());
        }
        players.clear();
        livingTeams.clear();
        SkyWars.getInstance().getPlayers().clear();
        topKills = new HashMap<>();
        timer = null;
        if (WorldManager.containsWorld(worldName + "_active")) {
            WorldManager.resetWorld(world);
            this.world = Bukkit.createWorld(new WorldCreator(worldName + "_active"));
            this.world.setGameRuleValue("doDaylightCycle", "false");
            this.world.setGameRuleValue("doWeatherCycle", "false");
        }
        for (SWTeam team : getTeams()) {
            team.setSpawn(new Location(world, team.getSpawn().getX(), team.getSpawn().getY(), team.getSpawn().getZ()));
            team.setBox();
            team.clear();
        }
        setStatus(GameStatus.LOBBY);
    }

    public void spawnDragon() {
        EnderDragon dragon = (EnderDragon) GameManager.getGame().getWorld().spawnEntity(GameManager.getGame().getLobby(), EntityType.ENDER_DRAGON);
        dragon.setPhase(Phase.LEAVE_PORTAL);
        sendMessage("&rв мире &b[&e+1&b] &cдракон&r!");
    }

    public void setTimer(Timer timer) {
        if (this.timer != null) this.timer.cancel();
        this.timer = timer;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public Set<SWPlayer> getPlayers() {
        return players;
    }

    public boolean containsPlayer(Player p) {
        for (SWPlayer player : players) {
            if (player.getPlayer() == p) return true;
        }
        return false;
    }

    public boolean containsChest(SWLocation chest) {
        for (SWLocation c : chests) {
            if (c.equals(chest)) {
                return true;
            }
        }
        return false;
    }

    public SWPlayer getPlayer(Player p) {
        for (SWPlayer player : players) {
            if (player.getPlayer() == p) return player;
        }
        return null;
    }

    public List<SWTeam> getLivingTeams() {
        return livingTeams;
    }

    public List<SWTeam> getTeams() {
        return teams;
    }

    public List<SWLocation> getChests() {
        return chests;
    }

    public void addTeams(SWTeam team) {
        this.teams.add(team);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean isStatus(GameStatus status) {
        return getStatus() == status;
    }

    public void sendMessage(String message) {
        getPlayers().forEach(p -> {
            p.sendMessage(ChatUtil.format(message));
        });
    }

    public World getWorld() {
        return world;
    }

    public Location getLobby() {
        return gameLobbyPoint;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setLobby(Location loc) {
        this.gameLobbyPoint = loc;
    }

    public void setPlayers(int maxPlayers, int minPlayers) {
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public void forceStart() {
        if (timer == null) return;
        if (isStatus(GameStatus.STARTING)) {
            if (timer.time > 3) timer.time = 1;
        }
    }

    public SWTeam getTeam(Location loc) {
        double distance = 100;
        SWTeam team = null;
        for (SWTeam t : teams) {
            if (t.getSpawn().distance(loc) < distance) {
                team = t;
                distance = t.getSpawn().distance(loc);
            }
        }
        return team;
    }

    public Map<Location, SWChestInfo> getChestsInfo() {
        return chestsInfo;
    }

    @SuppressWarnings("deprecation")
    public void fillChests() {
        PacketHandler.openChest = false;
        for (SWLocation chestLoc : chests) {
            Location loc = new Location(world, chestLoc.getX(), chestLoc.getY(), chestLoc.getZ());
            loc.getBlock().setType(Material.CHEST);
            Chest chest = (Chest) loc.getBlock().getState();
            loc.getBlock().setData(chestLoc.getData());
            ChestManager.getChestManager().fillChests(chest);
            NMS.changeChestState(loc, false);
        }
        for (SWTeam team : teams) {
            List<Chest> chests = new ArrayList<>();
            for (SWLocation c : team.getChests()) {
                Location loc = new Location(world, c.getX(), c.getY(), c.getZ());
                loc.getBlock().setType(Material.CHEST);
                Chest chest = (Chest) loc.getBlock().getState();
                loc.getBlock().setData(c.getData());
                chests.add(chest);
                NMS.changeChestState(loc, false);
            }
            ChestManager.getChestManager().fillTeamChest(chests);
        }
        for (SWPlayer player : getPlayers()) {
            player.getScoreboard().updateStatusGameScoreboard();
            NMS.sendTitle(player.getPlayer(), "", TitleAction.TITLE);
            NMS.sendTitle(player.getPlayer(), "&a&lСУНДУКИ ЗАПОЛНЕНЫ!", TitleAction.TITLE);
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
        }
        PacketHandler.openChest = true;
        if (chestsInfo.keySet().size() == 0) return;
        for (Location loc : chestsInfo.keySet()) {
            chestsInfo.get(loc).clear();
        }
        chestsInfo.clear();
    }

    public enum GameStatus {
        LOBBY(0), STARTING(15), REFILL_CHESTS_1(180), REFILL_CHESTS_2(120), DRAGONS(120), ENDING(300), RELOAD(5);
        private int time;

        GameStatus(int time) {
            this.time = time;
        }

        public int getTime() {
            return time;
        }
    }

    public void addTopKilss(String name, int kills) {
        topKills.put(name, kills);
    }

    public void addKills(String name, int kills) {
        topKills.replace(name, kills);
    }
}
