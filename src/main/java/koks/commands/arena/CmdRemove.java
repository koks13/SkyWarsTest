package koks.commands.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.managers.GameManager;
import koks.commands._Command;

public class CmdRemove extends _Command{
	
	public CmdRemove() {
		super("remove", "<имя_арены>:Удаляет арену");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(notPlayer);
			return;
		}
		Player p = (Player) sender;
		if(args.length != 1) {
			p.sendMessage("§b/" + "sw" + " " + getName() + " §e<имя_арены>");
			return;
		}
		String name = args[0];
		if(GameManager.getGame() == null) {
			p.sendMessage("§cАрена не создана!!!");
			p.sendMessage("§b/sw create §e<название_мира>");
			return;
		}
		GameManager.removeGame();
		p.sendMessage("§aВы удалили арену §6[§e" + name + "§6]");
	}

}
