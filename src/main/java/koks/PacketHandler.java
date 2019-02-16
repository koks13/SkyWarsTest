package koks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;

import com.comphenix.protocol.events.PacketEvent;
import koks.managers.GameManager;
import koks.objects.SWGame;

public class PacketHandler{

	public static boolean openChest = true;
	
	public static void chestInteractPacketHandler() {
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.addPacketListener(
				new PacketAdapter(SkyWars.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.BLOCK_ACTION) {

					@Override
					public void onPacketSending(PacketEvent event) {
						if(event.getPacketType() == PacketType.Play.Server.BLOCK_ACTION) {
							if(openChest && event.getPacket().getModifier().getValues().get(2).toString().equalsIgnoreCase("0")){
								SWGame game = GameManager.getGame();
								if(game == null) return;
								if(game.isStatus(SWGame.GameStatus.LOBBY)|| game.isStatus(SWGame.GameStatus.STARTING)) return;
								event.setCancelled(true);
							}
						}
					}
				}
				);
	}
}
