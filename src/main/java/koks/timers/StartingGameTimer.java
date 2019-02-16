package koks.timers;

import org.bukkit.Sound;

import koks.objects.SWGame;
import koks.utility.NMS;
import koks.utility.NMS.TitleAction;

public class StartingGameTimer extends Timer {
	
    public StartingGameTimer(int time, SWGame game) {
        super(time, game);
    }
    
    @Override
    protected void onStart() {
    	boolean check = game.playerCheck();
    	if(check) {
    		//game.scoreboard.updateScoreboard(time);
    		game.getPlayers().forEach(p -> p.getScoreboard().updateLobbyScoreboard(time));
    		if(time == 15 || time == 10 || time == 5) {
    			game.getPlayers().forEach(player ->{
    				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BASS, 2, 2);
    				NMS.sendTitle(player.getPlayer(), "&e" + time + " &lсекунд", TitleAction.TITLE);
    				//NMS.sendTitle(player.getPlayer(), "&e&lдо начала&c!", TitleAction.SUBTITLE);
    			});
    		}
    		if(time <= 3 && time != 0) {
    			game.getPlayers().forEach(player ->{
    				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BASS, 2, 2);
    				NMS.sendTitle(player.getPlayer(), "&c&l" + time, TitleAction.TITLE);
    				//NMS.sendTitle(player.getPlayer(), "&c&lПРИГОТОВСТЕСЬ!", TitleAction.SUBTITLE);
    			});
    		}
    	} else {
    		//game.scoreboard.updateScoreboard(0);
    		game.getPlayers().forEach(p -> p.getScoreboard().updateLobbyScoreboard());
    		cancel();
    	}
    }
    
    @Override
    protected void onStop() {
    	game.startGame();
    	cancel();
    }
}
