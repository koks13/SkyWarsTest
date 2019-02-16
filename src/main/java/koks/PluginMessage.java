package koks;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PluginMessage implements PluginMessageListener {
    private static SkyWars plugin = SkyWars.getInstance();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;
    }

    public void connect(Player player) {
        String serverName = plugin.getConfig().getString("LobbyServer");
        ByteArrayDataOutput serverconnect = ByteStreams.newDataOutput();

        serverconnect.writeUTF("Connect");
        serverconnect.writeUTF(serverName);
        player.sendPluginMessage(plugin, "BungeeCord", serverconnect.toByteArray());

    }
}
