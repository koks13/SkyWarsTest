package koks.objects;

import koks.utility.ChatUtil;

import java.util.ArrayList;
import java.util.List;

public class SWStats {

    private int kills;
    private int deaths;
    private int games;
    private int wins;
    private int coins;
    private int exp;
    private int level;
    private int souls;
    private final List<SWKit> kits = new ArrayList<>();

    public SWStats() {
        this.level = 1;
    }

    public void setStats(int kills, int deaths, int games, int wins, int coins, int exp, int level, int souls) {
        this.kills = kills;
        this.deaths = deaths;
        this.games = games;
        this.wins = wins;
        this.coins = coins;
        this.exp = exp;
        this.level = level;
        this.souls = souls;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getGames() {
        return games;
    }

    public int getWins() {
        return wins;
    }

    public int getCoins() {
        return coins;
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public int getSouls() {
        return souls;
    }

    public void addKills(int kills) {
        this.kills = this.kills + kills;
    }

    public void addDeaths() {
        this.deaths++;
    }

    public void addWins() {
        this.wins++;
    }

    public void addGames() {
        this.games++;
    }

    public void addCoins(int coins) {
        this.coins = this.coins + coins;
    }

    public void addSouls(int souls){ this.souls=+souls; }

    public void addЕxperience(int exp){ this.exp=+exp; }

    public void addLevel(){
        this.level++;
    }

    public void addKits(SWKit kit) {
        if (!kits.contains(kit)) kits.add(kit);
    }

    public boolean containsKit(SWKit kit) {
        String kitName = kit.getKitName();
        for (SWKit k : kits) {
            if (k.getKitName().equalsIgnoreCase(kitName)) return true;
        }
        return false;
    }

    public SWKit getKit(String name) {
        for (SWKit kit : kits) {
            if (name.equalsIgnoreCase(ChatUtil.clearColor(kit.getKitName()))) return kit;
        }
        return null;
    }

    public List<SWKit> getKits() {
        return kits;
    }
}
