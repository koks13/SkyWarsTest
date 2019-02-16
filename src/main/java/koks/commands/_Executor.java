package koks.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class _Executor implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmdName = null;
		if(args.length == 0) cmdName = "help";
		else cmdName = args[0];
		
		// ---------------------------Help
		if(cmdName.equalsIgnoreCase("help")) {
			sender.sendMessage("§b==================================================");
			
			sender.sendMessage("§cSky§aWars §ehelp:");
			sender.sendMessage(" ");
			
			for(_Command cmd : _Commands.cmdList()) {
				String name = cmd.getName();
				String[] descriptions = cmd.getDescription().split(":");
				
				String arg = descriptions[0], description = descriptions[1];
				
				sender.sendMessage("§b/" + command.getName() + " " + name + " §e" + arg);
				sender.sendMessage(" " + description);
			}
			
			sender.sendMessage(" ");
			sender.sendMessage("§b==================================================");
			return true;
		}
		//\---------------------------Help
		
		List<String> cmdArgsArray = new ArrayList<>();
		for(String arg : args) {
			cmdArgsArray.add(arg);
		}
		cmdArgsArray.remove(0);
		String[] cmdArgs = cmdArgsArray.toArray(new String[0]);
		
		Map<String, _Command> aliases = _Commands.getAliases();
		if(aliases.containsKey(cmdName)) return executeCmd(aliases.get(cmdName), sender, cmdArgs);
		
		_Command cmd = _Commands.findCmd(cmdName);
		if(cmd != null) return executeCmd(cmd, sender, cmdArgs);
		
		sender.sendMessage("§cНеизвестная команда!");
		return true;
	}

	private boolean executeCmd(_Command cmd, CommandSender sender, String[] cmdArgs) {
		if(!sender.isOp()) {
			sender.sendMessage(cmd.hasntPermission);
			return true;
		}
		cmd.execute(sender, cmdArgs);
		return false;
	}
	
}
