package koks.timers;

import koks.SkyWars;
import koks.objects.SWGame;

public class EndGameTimer extends Timer{

	private int maxTime;
	private int dragons;
	
	public EndGameTimer(int time, SWGame game) {
		super(time, game);
		maxTime = time;
	}

	@Override
	protected void onStart() {
		game.getPlayers().forEach(p -> p.getScoreboard().updateGameScoreboard(time));
		game.winnerCheck();
		if(time == maxTime && dragons < 4) {
			game.spawnDragon();
			dragons++;
			maxTime = maxTime-30;
		}
	}

	@Override
	protected void onStop() {
		game.getLivingTeams().clear();
		game.winnerCheck();
		cancel();
	}
}
