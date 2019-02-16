package koks.commands.test;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.SkyWars;
import koks.commands._Command;
import koks.managers.GameManager;
import koks.objects.SWGame;
import koks.objects.SWPlayer;
public class CmdLeave extends _Command{
	
	public CmdLeave() {
		super("leave", "<имя_арены> <ник_игрока>:Удаляет указаного игрока с арены");
		//_Commands.addAlias(this, "np");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(notPlayer);
			return;
		}
		Player p = (Player) sender;
		if(args.length != 0) {
			p.sendMessage("§b/" + "sw" + " " + getName());
			return;
		}
		SWPlayer player = SkyWars.getInstance().getPlayer(p);
		SWGame game = GameManager.getGame();
		if(player == null || game == null) {
			return;
		}
		if(!game.containsPlayer(p)) {
			p.sendMessage("§cВас нет на этой арене!");
		}else {
			game.leave(player);
		}
	}
}
