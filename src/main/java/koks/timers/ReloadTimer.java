package koks.timers;

import koks.objects.SWGame;

public class ReloadTimer extends Timer{
	
	public ReloadTimer(int time, SWGame game) {
		super(time, game);
	}

	@Override
	protected void onStart() {
		game.sendMessage("&cперезагрузка через &4" + time + " &cсекунд!");
	}

	@Override
	protected void onStop() {
		game.reload();
		cancel();
	}
	

}
