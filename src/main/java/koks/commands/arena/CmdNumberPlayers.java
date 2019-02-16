package koks.commands.arena;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import koks.managers.GameManager;
import koks.commands._Command;
import koks.commands._Commands;
import koks.objects.SWGame;


public class CmdNumberPlayers extends _Command{
	
	public CmdNumberPlayers() {
		super("numberplayers", "<минимальное_количество_игроков> <максимальное_количество_игроков>:Устанавливает максимальное и минимальное количество игроков на арене");
		_Commands.addAlias(this, "np");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(notPlayer);
			return;
		}
		Player p = (Player) sender;
		if(args.length != 2) {
			p.sendMessage("§b/" + "sw" + " " + getName() + " §e<минимальное_количество_игроков> <максимальное_количество_игроков>");
			return;
		}
		SWGame game = GameManager.getGame();
		if(game == null) {
			p.sendMessage("§cАрена не создана!!");
			return;
		}
		int minP = 2, maxP = 12;
		try {
			minP = Integer.parseInt(args[0]);
			maxP = Integer.parseInt(args[1]);
		}catch(NumberFormatException e) {
			p.sendMessage("§cКоличество игроков нужно указывать цифрами!");
			return;
		}
		game.setPlayers(minP, maxP);
		p.sendMessage("§aМинимальное количество игроков на арене теперь §c" + minP);
		p.sendMessage("§aМаксимальное количество игроков на арене теперь §c" + maxP);
	}

}
