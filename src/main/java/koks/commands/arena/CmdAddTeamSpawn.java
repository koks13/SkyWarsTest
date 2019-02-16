package koks.commands.arena;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.commands._Command;
import koks.commands._Commands;
import koks.managers.GameManager;
import koks.objects.SWGame;
import koks.objects.SWTeam;

public class CmdAddTeamSpawn extends _Command {
	
	public CmdAddTeamSpawn() {
		super("addteam", ":Добавляет команду и устанавливает колбу в которую будут телепортированы команды перед началом игры");
		_Commands.addAlias(this, "at");
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
		Location loc = p.getLocation();
		loc.setPitch(2);
		SWGame game = GameManager.getGame();
		if(game == null) {
			p.sendMessage("§cАрена не создана!!");
			return;
		}
		int id = game.getTeams().size()+1;
		game.addTeams(new SWTeam(id, loc));
		p.sendMessage("§aВы добавили команду §6[§e" + id + "§6]");
		p.sendMessage("§aВы добавили спавн команды §6[§e" + id + "§6]");
	}

}
