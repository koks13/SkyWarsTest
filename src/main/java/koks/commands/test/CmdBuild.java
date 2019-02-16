package koks.commands.test;

import koks.SkyWars;
import koks.commands._Command;
import koks.utility.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBuild extends _Command {

    public CmdBuild() {
        super("build", ":Включает/выключает режим билдера");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(notPlayer);
            return;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.hasPermission("sw.build")) {
                p.addAttachment(SkyWars.getInstance(), "sw.build", false);
                p.sendMessage(ChatUtil.format("&cРежим билдера выключен!"));
            } else {
                p.addAttachment(SkyWars.getInstance(), "sw.build", true);
                p.sendMessage(ChatUtil.format("&aРежим билдера включен!"));
            }
        }else {
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null) {
                if (target.hasPermission("sw.build")) {
                    target.addAttachment(SkyWars.getInstance(), "sw.build", false);
                    p.sendMessage(ChatUtil.format("&cВы выключили режим билдера игроку " + target.getName()));
                    target.sendMessage(ChatUtil.format("&cВам выключил режим билдера " + p.getName() + "!"));
                } else {
                    target.addAttachment(SkyWars.getInstance(), "sw.build", true);
                    p.sendMessage(ChatUtil.format("&aВы включили режим билдера игроку " + target.getName()));
                    target.sendMessage(ChatUtil.format("&aВам включил режим билдера " + p.getName() + "!"));
                }
            }
        }
    }
}
