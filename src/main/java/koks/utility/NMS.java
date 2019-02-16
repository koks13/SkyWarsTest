package koks.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockAction;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;

public class NMS {
	
	public static void sendTitle(Player player, String message, TitleAction titleAction) {
		PacketPlayOutTitle title = null;
		switch(titleAction) {
		case TITLE: 
			title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"" + ChatUtil.format(message) + "\"}"),20,20,20);
			break;
		case TIMES:
			title = new PacketPlayOutTitle(EnumTitleAction.TIMES, ChatSerializer.a("{\"text\":\"" + ChatUtil.format(message) + "\"}"),20,20,20);
			break;
		case SUBTITLE:
			title = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"" + ChatUtil.format(message) + "\"}"),20,20,20);
			break;
		case ACTIONBAR:
			title = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, ChatSerializer.a("{\"text\":\"" + ChatUtil.format(message) + "\"}"),20,20,20);
			break;
		case RESET:
			title = new PacketPlayOutTitle(EnumTitleAction.RESET, ChatSerializer.a("{\"text\":\"" + ChatUtil.format(message) + "\"}"),20,20,20);
			break;
		case CLEAR:
			title = new PacketPlayOutTitle(EnumTitleAction.CLEAR, ChatSerializer.a("{\"text\":\"" + ChatUtil.format(message) + "\"}"),20,20,20);
			break;
		}											
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);	
	}
	public enum TitleAction{
		TITLE, TIMES, SUBTITLE, ACTIONBAR, RESET, CLEAR
	}
	public static void changeChestState(Location loc, boolean open) {
		BlockPosition pos = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		PacketPlayOutBlockAction packet = new PacketPlayOutBlockAction(pos, Blocks.CHEST, 1, open ? 1 : 0);
		for(Player player: Bukkit.getOnlinePlayers()) 
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
}
