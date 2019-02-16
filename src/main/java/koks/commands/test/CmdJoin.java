package koks.commands.test;

import koks.SkyWars;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.managers.GameManager;
import koks.commands._Command;
import koks.objects.SWGame;
import koks.objects.SWPlayer;

public class CmdJoin extends _Command {

    public CmdJoin() {
        super("join", "<имя_арены>:Добавляет вас на арену");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(notPlayer);
            return;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            SWGame game = GameManager.getGame();
            if (game == null) {
                p.sendMessage("§cАрена не создана!!");
                return;
            }
            SWPlayer player = SkyWars.getInstance().getPlayer(p);
            if (player == null) {
                player = new SWPlayer(p);
                SkyWars.getInstance().addPlayer(player);
                game.join(player);
            } else if (player.isGame()) {
                p.sendMessage("§cВы уже находитесь на арене!");
            } else {
                game.join(player);
            }
        } else p.sendMessage("§b/" + "sw " + getName());
    }
}
