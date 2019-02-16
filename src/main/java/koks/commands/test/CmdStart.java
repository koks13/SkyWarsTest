package koks.commands.test;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.managers.GameManager;
import koks.commands._Command;
import koks.objects.SWGame;

public class CmdStart extends _Command {
	
	public CmdStart() {
		super("start", "<имя_арены>:Начинает сразу игру");
		//_Commands.addAlias(this, "ats");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(notPlayer);
			return;
		}
		Player p = (Player) sender;
		
		if(args.length > 1) {
			p.sendMessage("§b/" + "sw" + " " + getName() + " §e<имя_арены>");
			return;
		}
		if(args.length == 0) {
			SWGame game = GameManager.getGame();
			if(game == null) {
				p.sendMessage("§cНет арены!!");
				return;
			}
			game.forceStart();
			return;
		}
	}
}
