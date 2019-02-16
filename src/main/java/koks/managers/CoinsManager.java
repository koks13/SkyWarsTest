package koks.managers;

import koks.SkyWars;
import koks.objects.SWPlayer;

public class CoinsManager {

    public static void coinsAdder(SWPlayer player, Cause cause) {
        if(cause.getCoins() != 0) {
            player.addCoins(cause.getCoins());
            player.sendMessage("&aВы получили &e" + cause.getCoins() + " коинов!");
        }
    }

    public enum Cause {

        KILL(SkyWars.getInstance().getConfig().getInt("coins.kill")), DEATH(SkyWars.getInstance().getConfig().getInt("coins.death")), WIN(SkyWars.getInstance().getConfig().getInt("coins.win"));

        private float coins;
        Cause(float coins) {
            this.coins = coins;
        }
        public float getCoins() {
            return coins;
        }
    }
}
