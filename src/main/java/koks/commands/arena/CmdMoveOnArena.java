package koks.commands.arena;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.commands._Command;
import koks.managers.GameManager;
import koks.objects.SWGame;

public class CmdMoveOnArena extends _Command{
	
	public CmdMoveOnArena() {
		super("move", "<имя_арены>:Телепортирует вас в указанный мир");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(notPlayer);
			return;
		}
		Player p = (Player) sender;
		if(args.length != 1) {
			p.sendMessage("§b/" + "sw" + " " + getName() + " §e<название_мира>");
			return;
		}

		World world = Bukkit.getWorld(args[0]);
		if(world == null) {
			p.sendMessage("§cМир с названием §4" + args[0] + "§c не загружен!!");
			return;
		}
		p.teleport(world.getSpawnLocation());
	}
}
