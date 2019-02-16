package koks.managers;

import koks.SkyWars;
import koks.objects.SWPlayer;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

public class ExperienceManager {

    private static Map<Integer, Integer> levels = new HashMap<>();

    public static void experienceAdder(SWPlayer player, ExperienceManager.Cause cause) {
        if (cause.getExperience() != 0) {
            player.addЕxperience(cause.getExperience());
            player.sendMessage("&aВы получили &5" + cause.getExperience() + " &aопыта!");
            if(player.getStats().getExp() >= levels.get(player.getStats().getLevel()+1)){
                player.getStats().addЕxperience(-levels.get(player.getStats().getLevel()+1));
                player.getStats().addLevel();
                player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1 , 1);
                player.sendMessage("&b===============================");
                player.sendMessage(" ");
                player.sendMessage("   &lВаш уровень повышен");
                player.sendMessage("   &lНовый уровень &b[&e" + player.getStats().getLevel() + "&b]");
                player.sendMessage("   &lДо следующего уровня нужно &5" + levels.get(player.getStats().getLevel()+1));
                player.sendMessage(" ");
                player.sendMessage("&b===============================");
            }
        }
    }

    public static void loadLevels() {
        int level = 1;
        for (int experience : SkyWars.getInstance().getConfig().getIntegerList("levels")) {
            levels.put(level, experience);
            level++;
        }
    }

    public enum Cause {

        KILL(SkyWars.getInstance().getConfig().getInt("experience.kill")), DEATH(SkyWars.getInstance().getConfig().getInt("experience.death")), WIN(SkyWars.getInstance().getConfig().getInt("experience.win"));

        private float experience;

        Cause(float experience) {
            this.experience = experience;
        }

        public float getExperience() {
            return experience;
        }
    }
}
