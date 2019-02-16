package koks.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import koks.commands.arena.CmdAddTeamSpawn;
import koks.commands.arena.CmdCreate;
import koks.commands.arena.CmdMoveOnArena;
import koks.commands.arena.CmdNumberPlayers;
import koks.commands.arena.CmdRemove;
import koks.commands.arena.CmdSetLobby;
import koks.commands.arena.CmdSetTeamSpawn;
import koks.commands.test.CmdBuild;
import koks.commands.test.CmdJoin;
import koks.commands.test.CmdLeave;
import koks.commands.test.CmdStart;

public class _Commands {
	
	private static List<_Command> commands = new ArrayList<>();
	private static Map<String, _Command> aliases = new HashMap<>();
	
	public static void registerCmds() {
		registerCmd(new CmdCreate());
		registerCmd(new CmdNumberPlayers());
		registerCmd(new CmdSetLobby());
		registerCmd(new CmdAddTeamSpawn());
		registerCmd(new CmdSetTeamSpawn());
		registerCmd(new CmdRemove());
		registerCmd(new CmdMoveOnArena());
		
		registerCmd(new CmdJoin());
		registerCmd(new CmdLeave());
		registerCmd(new CmdStart());

		registerCmd(new CmdBuild());
	}
	
	public static void registerCmd(_Command cmd) {
		if(!commands.contains(cmd)) {
			commands.add(cmd);
		}
	}

	public static _Command findCmd(String name) {
		for(_Command cmd : commands) {
			if(cmd.getName().equalsIgnoreCase(name)) return cmd;
		}
		return null;
	}
	
	public static void addAlias(_Command cmd, String alias) {
		aliases.put(alias, cmd);
	}
	
	public static List<_Command> cmdList() { return commands; }
	public static Map<String, _Command> getAliases() { return aliases; }
	
}
