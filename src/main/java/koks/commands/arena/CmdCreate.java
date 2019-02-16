package koks.commands.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.commands._Command;
import koks.commands._Commands;
import koks.managers.GameManager;
import koks.managers.WorldManager;


public class CmdCreate extends _Command {
	public CmdCreate() {
		super("create", "<название_мира>:Создаёт арену в указаном мире");
		_Commands.addAlias(this, "cr");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(notPlayer);
			return;
		}
		Player p = (Player) sender;
		if (args.length != 1) {
			p.sendMessage("§b/sw " + getName() + " §e<название_мира>");
			return;
		}
		String name = args[0];
		if (!WorldManager.containsWorld(name)) {
			p.sendMessage("§c§lНе найдено карты с таким названием!!");
			p.sendMessage("§6[§a§lСписок карт§6]");
			for (String worldName : WorldManager.getMaps()) p.sendMessage("§e" + worldName);
			return;
		}
		if (GameManager.getGame() != null) {
			p.sendMessage("§cАрена уже существует!!");
			p.sendMessage("§b/sw remove §e<имя_арены>");
			return;
		}
		GameManager.createGame(name);
		p.sendMessage("§aВы создали арену §6[§e" + name + "§6]");
		p.teleport(GameManager.getGame().getWorld().getSpawnLocation());
	}
}

