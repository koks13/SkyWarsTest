package koks.timers;

import org.bukkit.Bukkit;

import koks.SkyWars;
import koks.objects.SWGame;

public abstract class Timer implements Runnable {
	
	public int time;
	protected final SWGame game;
	protected final int sched;

	public Timer(int time, SWGame game) {
		this.game = game;
		this.time = time;
		sched = Bukkit.getScheduler().scheduleSyncRepeatingTask(SkyWars.getInstance(), (Runnable) this, 20L, 20L);
	}

	@Override
	public void run() {
		if (time >= 0) {
			onStart();
			time--;
		} else this.onStop();	
	}
	
	public void cancel() {
		Bukkit.getScheduler().cancelTask(sched);
	}

	protected abstract void onStart();

	protected abstract void onStop();

	
	
}
