package koks.timers;

import koks.objects.SWGame;
import koks.objects.SWGame.GameStatus;

public class GameDragonsTimer extends Timer{

	public GameDragonsTimer(int time, SWGame game) {
		super(time, game);
		
	}
	@Override
	protected void onStart() {
		game.getPlayers().forEach(p -> p.getScoreboard().updateGameScoreboard(time));
		game.winnerCheck();
	}

	@Override
	protected void onStop() {
		game.setStatus(GameStatus.ENDING);
		game.getPlayers().forEach(p -> p.getScoreboard().updateStatusGameScoreboard());
		game.setTimer(new EndGameTimer(GameStatus.ENDING.getTime(), game));
		cancel();
	}
}
