package koks.objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import koks.SkyWars;
import koks.objects.SWGame.GameStatus;

public class SWScoreboard {

    private SWPlayer player;
    private Scoreboard b;
    private Objective o;
    private final SWGame game;
    private Team players;
    private Team time;
    private Team event;
    private Team kills;
    private Team kit;
    
    public SWScoreboard(SWGame game, SWPlayer player) {
        this.game = game;
        this.player = player;
        createBoard();
        if(game.isStatus(GameStatus.LOBBY) || game.isStatus(GameStatus.STARTING)) {
        	createLobbyScoreboard();
        } else createGameScoreboard();
    }
    
    private void createBoard(){
        b = Bukkit.getScoreboardManager().getNewScoreboard();
        o = b.registerNewObjective("skywars", "dummy");
        o.setDisplaySlot(DisplaySlot.SIDEBAR);
        o.setDisplayName("§bSkyWars");
        animatedScoreboard();
    }
    
    private void createLobbyScoreboard() {
    	if(b == null) createBoard();
        o.getScore(" §b§lКарта§b: §r" + game.getWorldName()).setScore(7);

        players = b.registerNewTeam("players");
        players.addEntry(" §b§lИгроков§b: §a");
        players.setSuffix(game.getPlayers().size() + "/" + game.getMaxPlayers());
        o.getScore(" §b§lИгроков§b: §a").setScore(6);

        kit = b.registerNewTeam("kit");
        kit.addEntry(" §b§lКит§b: ");
        if(player.getSelectedKit() != null) kit.setSuffix(player.getSelectedKit().getKitName());
        o.getScore(" §b§lКит§b: ").setScore(4);

        time = b.registerNewTeam("timer");     
        time.addEntry("§e§l");
        time.setPrefix(" §e§lОжидание");
        time.setSuffix(" игроков");
        o.getScore("§e§l").setScore(2);

        o.getScore("    ").setScore(8);
        o.getScore("   ").setScore(5);
        o.getScore("  ").setScore(3);
        o.getScore(" ").setScore(1);
    }

    public void createGameScoreboard() {
    	if(b == null) createBoard();
    	b.getTeams().forEach(t -> t.unregister());

    	b.resetScores(" §b§lКит§b: ");
        b.resetScores("§e§l");

    	o.getScore(" §r§lСтатус: ").setScore(8);
    	event = b.registerNewTeam("event");
    	event.addEntry("stand: ");
    	event.addEntry(" §a§lРеспавн сундуков: §a");
    	event.addEntry(" §e§lРеспавн сундуков: §e");
    	event.addEntry(" §c§lДраконы: §c");
    	event.addEntry(" §c§lКонец: §c");
    	event.setSuffix("00:00");
        o.getScore(" §a§lРеспавн сундуков: §a").setScore(7);
        
        players = b.registerNewTeam("players");
        players.addEntry(" §b§lИгроков§b: §a");
        players.setSuffix("" + game.getLivingTeams().size());
        o.getScore(" §b§lИгроков§b: §a").setScore(5);
        
        kills = b.registerNewTeam("kills");
        kills.addEntry(" §r§lУбийств: §a");
        kills.setSuffix("0");
        o.getScore(" §r§lУбийств: §a").setScore(4);
        
        o.getScore( " §b§lКарта§b: §r" + game.getWorldName()).setScore(2);
        
        o.getScore("    ").setScore(9);
        o.getScore("   ").setScore(6);
        o.getScore("  ").setScore(3);
        o.getScore(" ").setScore(1);
        
        if(player == null) return;
        
        Team allies = b.registerNewTeam("allies");
        allies.setAllowFriendlyFire(false);
        allies.setCanSeeFriendlyInvisibles(true);
        allies.setPrefix("§a");

        Team enemy = b.registerNewTeam("enemy");
        enemy.setAllowFriendlyFire(true);
        enemy.setCanSeeFriendlyInvisibles(false);
        enemy.setPrefix("§c");
        
        for(SWTeam team : game.getLivingTeams()) {
        	if(team.getPlayer().getName().equalsIgnoreCase(player.getName())) {
        		allies.addEntry(player.getName());
        		continue;
        	}
        	enemy.addEntry(team.getPlayer().getName());
        }
    }
    
    public void addStand(String uuid) {
    	event.addEntry(uuid);
    }
    public void removeStand(String uuid) {
    	event.removeEntry(uuid);
    }
    
    public void updateStatusGameScoreboard() {
    	if(game.isStatus(GameStatus.REFILL_CHESTS_2)) {
			b.resetScores(" §a§lРеспавн сундуков: §a");
			o.getScore(" §e§lРеспавн сундуков: §e").setScore(7);
    	} else if(game.isStatus(GameStatus.DRAGONS)) {
			b.resetScores(" §e§lРеспавн сундуков: §e");
			o.getScore(" §c§lДраконы: §c").setScore(7);
    	} else if(game.isStatus(GameStatus.ENDING)) {
			b.resetScores(" §c§lДраконы: §c");
			o.getScore(" §c§lКонец: §c").setScore(7);
    	}
    }
    
    public void updateKills(int kills) {
    	this.kills.setSuffix("§a" + kills);
    }
    
    public void updateGameScoreboard(int time) {
    	if(time > 0) event.setSuffix(secToMin(time));
    	else event.setSuffix("00:00");
    	players.setSuffix("§a" + game.getLivingTeams().size());
    }
    
    public void updateLobbyScoreboard() {
    	players.setSuffix(game.getPlayers().size() + "/" + game.getMaxPlayers());
    	if(game.isStatus(GameStatus.LOBBY)) {
    		time.setPrefix(" §e§lОжидание");
    		time.setSuffix(" игроков");
    	}
    }

    public void updateKit(String kitName){
        kit.setSuffix(kitName);
    }

    public void updateLobbyScoreboard(int time) {    	
    	this.time.setPrefix(" §e§lСтарт:");
    	this.time.setSuffix(" §a" + secToMin(time));
    }
    
    public Scoreboard getScoreboard() { return b; }
    
    private String[] displayName = {
    		"§bSkyWars",
    		"S§bkyWars",
    		"Sk§byWars",
    		"Sky§bWars",
    		"SkyW§bars",
    		"SkyWa§brs",
    		"SkyWar§bs",
    		"SkyWars",
    		"§rSkyWars",
    		"§bS§rkyWars",
    		"§bSk§ryWars",
    		"§bSky§rWars",
    		"§bSkyW§rars",
    		"§bSkyWa§rrs",
    		"§bSkyWar§rs",
    		"§bSkyWars"};
    
    private void animatedScoreboard() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(SkyWars.getInstance(), new Runnable() {
			int i = 0;
			@Override
			public void run() {
				if(i == displayName.length) i = 0;
				o.setDisplayName(displayName[i++]);
			}
		}, 20, 3);
	}
    
	private String secToMin(int i) {
		int ms = i / 60;
		int ss = i % 60;
		String m = (ms < 10 ? "0" : "") + ms;
		String s = (ss < 10 ? "0" : "") + ss;
		String f = m + ":" + s;
		return f;
	}
}
