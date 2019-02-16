package koks.objects;

import java.util.UUID;

import koks.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import koks.utility.ChatUtil;
import koks.utility.NMS;
import koks.utility.NMS.TitleAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SWPlayer {

    private Player player;
    private SWGame game;
    private SWScoreboard scoreboard;
    private int kills, souls;
    private float exp, coins;
    private boolean spectator = false;
    private SWStats stats;
    private SWKit selectedKit;

    public SWPlayer(Player p) {
        this.player = p;
        stats = new SWStats();
    }

    public void savePlayerStats() {
        if (stats == null) return;
        stats.addKills(kills);
        if (SkyWars.getInstance().getMongoDb() != null) SkyWars.getInstance().getMongoDb().updatePlayerStats(this);
    }

    public SWStats getStats() {
        if (stats == null) return new SWStats();
        return stats;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isGame() {
        return game != null;
    }

    public SWGame getGame() {
        return game;
    }

    public void setGame(SWGame game) {
        this.game = game;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
        scoreboard.updateKills(kills);
        game.addKills(getName(), kills);
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }

    public void sendMessage(String message) {
        player.sendMessage(ChatUtil.format(message));
    }

    public void teleport(Location loc) {
        getPlayer().teleport(loc);
    }

    public String getName() {
        return player.getName();
    }

    public void createScoreboard() {
        this.scoreboard = new SWScoreboard(game, this);
        player.setScoreboard(this.scoreboard.getScoreboard());
    }

    public void removeScoreboard() {
        this.scoreboard = null;
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public SWScoreboard getScoreboard() {
        return scoreboard;
    }

    public void death() {
        NMS.sendTitle(player, "&cВЫ УМЕРЛИ!", TitleAction.TITLE);
        for (SWTeam team : game.getLivingTeams()) {
            if (team.removePlayer(this)) {
                game.getLivingTeams().remove(team);
                break;
            }
        }
    }

    public void selectKit(SWKit kit) {
        this.selectedKit = kit;
        if (scoreboard != null) scoreboard.updateKit(kit.getKitName());
    }

    public void giveKitItems() {
        if (selectedKit == null) return;
        if (selectedKit.getItemsInKit() == null && selectedKit.getItemsInKit().size() == 0) return;
        selectedKit.getItemsInKit().forEach(item -> player.getInventory().addItem(item));
    }

    public SWKit getSelectedKit() {
        return selectedKit;
    }

    public void openInventoryByKits() {
        if (stats.getKits().size() == 0) return;
        Inventory inv = Bukkit.createInventory(null, 27, ChatUtil.format("&b&lКИТЫ"));
        for (SWKit kit : stats.getKits()) {
            inv.addItem(kit.getDisplayItem());
        }
        player.openInventory(inv);
    }

    public void addSouls(int souls) {
        this.souls = this.souls+souls;
        stats.addSouls(souls);
    }

    public void addCoins(float coins) {
        this.coins = this.coins+coins;
        stats.addCoins((int) coins);
    }

    public void addЕxperience(float exp) {
        this.exp = this.exp+exp;
        stats.addЕxperience((int) exp);
    }

    public float getCoins() {
        return coins;
    }

    public float getExp() {
        return exp;
    }

    public int getSouls() {
        return souls;
    }
}
