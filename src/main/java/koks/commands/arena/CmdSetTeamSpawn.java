package koks.commands.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.managers.GameManager;
import koks.commands._Command;
import koks.commands._Commands;
import koks.objects.SWGame;

import org.bukkit.Location;

public class CmdSetTeamSpawn extends _Command {
	
	public CmdSetTeamSpawn() {
		super("setteamspawn", "<id_команды>:Устанавливает спавн команды (вы должны быть в колбе)");
		_Commands.addAlias(this, "sts");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(notPlayer);
			return;
		}
		Player p = (Player) sender;
		Location loc = p.getLocation();
		loc.setPitch(2);
		if(args.length != 1) {
			p.sendMessage("§b/" + "sw" + " " + getName() + " §e<id_команды>");
			return;
		}
		SWGame game = GameManager.getGame();
		if(game == null) {
			p.sendMessage("§cНет арены!!");
			return;
		}
		int id;
		try {
			id = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			p.sendMessage("§b/" + "sw" + " " + getName() + " §e<id_команды>");
			p.sendMessage("§cid нужно указывать цифрой");
			return;
		}
		if(game.getTeams().size() >= id && id > 0) {
			game.getTeams().get(id-1).setSpawn(loc);
			p.sendMessage("§aВы установили спавн команды §6[§e" + id + "§6]");
		}else {
			p.sendMessage("§cНет команды с id §4 " + id + "§c!!");
		}
	}

}
