package koks;

import java.util.*;

import koks.database.MongoDb;
import koks.listeners.*;
import koks.managers.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import koks.commands._Commands;
import koks.commands._Executor;
import koks.objects.SWGame;
import koks.objects.SWPlayer;
import koks.utility.ChatUtil;

public class SkyWars extends JavaPlugin {

    private static SkyWars instance;

    public static SkyWars getInstance() {
        return instance;
    }

    private MongoDb mongoDb;
    public MongoDb getMongoDb() {
        return mongoDb;
    }

    private boolean bungee;
    public boolean isBungee() {
        return bungee;
    }

    public PluginMessage pluginMessage;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        GameManager.setupGame();

        ChestManager.getChestManager().loadItems();
        KitManager.loadKits();

        getCommand("skywars").setExecutor(new _Executor());
        _Commands.registerCmds();

        loadListeners();
        ExperienceManager.loadLevels();

        if(getConfig().getBoolean("Mongodb.Enable")){
            mongoDb = new MongoDb();
            mongoDb.connection();
        }

        bungee = getConfig().getBoolean("Bungee");

        if(bungee) {
            pluginMessage = new PluginMessage();
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pluginMessage);
        }
    }

    @Override
    public void onDisable() {
        SWGame game = GameManager.getGame();
        if (game != null) {
            GameManager.saveGame();
            WorldManager.deleteWorld(game.getWorld());
        }
    }

    private static final String prefix = "[SkyWars] ";
    private final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    public void consoleSendMessage(String message) {
        console.sendMessage(ChatUtil.format(ChatColor.GREEN + prefix + ChatColor.WHITE + message));
    }

    private final Map<UUID, SWPlayer> players = new HashMap<>();

    public Map<UUID, SWPlayer> getPlayers(){ return players; }

    public SWPlayer getPlayer(Player player) {
        if (players.containsKey(player.getUniqueId())) return players.get(player.getUniqueId());
        return null;
    }

    public void addPlayer(SWPlayer player) {
        if (players.containsKey(player.getPlayer().getUniqueId())) return;
        players.put(player.getPlayer().getUniqueId(), player);
    }

    public void removePlayer(UUID uuid) {
        if (players.containsKey(uuid)) players.remove(uuid);
    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinE(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveE(), this);
        getServer().getPluginManager().registerEvents(new FoodLevelE(), this);
        getServer().getPluginManager().registerEvents(new PlayerDamageE(), this);
        getServer().getPluginManager().registerEvents(new MoveE(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathE(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractE(), this);
        getServer().getPluginManager().registerEvents(new ClickE(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceE(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakE(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseE(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropE(), this);
        PacketHandler.chestInteractPacketHandler();
    }
}
