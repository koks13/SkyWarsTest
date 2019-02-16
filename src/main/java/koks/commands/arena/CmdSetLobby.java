package koks.commands.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.managers.GameManager;
import koks.commands._Command;
import koks.commands._Commands;
import koks.objects.SWGame;

public class CmdSetLobby extends _Command{
	
	public CmdSetLobby() {
		super("setlobby", ":Создаёт точку спавна игроков, когда те заходят на арену");
		_Commands.addAlias(this, "sl");
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
		SWGame game = GameManager.getGame();
		if(game == null) {
			p.sendMessage("§cНет арены!!");
			return;
		}
		game.setLobby(p.getLocation());
		p.sendMessage("§aВы установили спавн игроков на арене §6[§e" + game.getWorldName() + "§6]");
	}

}
