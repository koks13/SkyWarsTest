package koks.timers;

import koks.objects.SWGame;
import koks.objects.SWGame.GameStatus;

public class GameTimer extends Timer {
	
    public GameTimer(int time, SWGame game) {
        super(time, game);
    }
    
    @Override
    protected void onStart() {
    	game.getPlayers().forEach(p -> p.getScoreboard().updateGameScoreboard(time));
    	game.winnerCheck();
    }
    
    @Override
    protected void onStop() {
    	if(game.isStatus(GameStatus.REFILL_CHESTS_1)) {
    		game.fillChests();
    		game.setStatus(GameStatus.REFILL_CHESTS_2);
    		game.getPlayers().forEach(p -> p.getScoreboard().updateStatusGameScoreboard());
    		time = GameStatus.REFILL_CHESTS_2.getTime();
    		return;
    	}
    	if(game.isStatus(GameStatus.REFILL_CHESTS_2)) {
    		game.fillChests();
    		game.setStatus(GameStatus.DRAGONS);
    		game.getPlayers().forEach(p -> p.getScoreboard().updateStatusGameScoreboard());
    		game.setTimer(new GameDragonsTimer(GameStatus.DRAGONS.getTime(), game));
    	}
    	cancel();
    }
}
