package dev.term4.mechanics.platform.client;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerPluginMessageEvent;

import java.nio.charset.StandardCharsets;

public final class VersionDetector {

    // This class should detect the protocol version (MAYBE client version if the client is using viaFabric, but this is spoofable) of the player on joining,
    //  and store it in the ClientInfoService map. This listener should only be active if configured (i.e. cross version play enabled)

    // For now only use protocol version sent from the proxy

    public static final String VIA_PROXY_DETAILS_CHANNEL = "vv:proxy_details";

    private static boolean registered = false; // default not registered

    private VersionDetector() {}

    public static void register(ClientInfoService clientInfo) {
        if (registered) return;
        registered = true;

        var handler = MinecraftServer.getGlobalEventHandler();

        handler.addListener(PlayerPluginMessageEvent.class, (event) -> {
            if (!VIA_PROXY_DETAILS_CHANNEL.equals(event.getIdentifier())) return;

            Player player = event.getPlayer();
            byte[] data = event.getMessage();
            if (data.length == 0) return;

            // Via payload is UTF-8 JSON
            String json = new String(data, StandardCharsets.UTF_8);
            clientInfo.setProxyDetails(player, json);
        });

        handler.addListener(PlayerDisconnectEvent.class, (event) -> clientInfo.remove(event.getPlayer()));
    }

}
