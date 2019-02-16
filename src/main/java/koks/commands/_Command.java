package koks.commands;

import org.bukkit.command.CommandSender;

public abstract class _Command {
	
	private String name;
	private String description;

	public final String hasntPermission = "§cНедостаточно прав!!!";
	public final String notPlayer = "§cOnly players can use this command!";
	
	public _Command(String name, String desc) {
		this.name = name;
		description = desc;
	}
	public String getName() { return name;}
	public String getDescription() { return description;}

	public abstract void execute(CommandSender sender, String[] args);
}
