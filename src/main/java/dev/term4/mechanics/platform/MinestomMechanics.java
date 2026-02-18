package dev.term4.mechanics.platform;

import dev.term4.mechanics.platform.client.ClientInfoService;
import dev.term4.mechanics.platform.client.VersionDetector;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public final class MinestomMechanics {

    // This is the main initialization class for the library.
    //  This class allows the user to enable / disable server level systems (version detection, maybe packet interception, nothing else for now)

    // Server level options (defaults)
    /** When enabled the server listens for player details sent from ViaVersion proxy message */
    public boolean viaProxyDetails = false; // false by default

    // might add an option for packet validation when not using a proxy? probably better to use a separate library for that though

    private final EventNode<Event> root = EventNode.all("MinestomMechanics");

    // Server level services
    private ClientInfoService clientInfo;

    private static final MinestomMechanics INSTANCE = new MinestomMechanics();
    private boolean initialized = false;

    private MinestomMechanics() {}

    public static MinestomMechanics getInstance() { return INSTANCE; }

    /** Initialize with current options (or defaults if no options specified) */
    public void initialize() {
        if (initialized) return;
        initialized = true;

        clientInfo = new ClientInfoService();

        // Attempt at Nodes for simpler detector modules
        MinecraftServer.getGlobalEventHandler().addChild(root);

        EventNode<Event> detectors = EventNode.all("detectors");
        root.addChild(detectors);

        root.addListener(PlayerDisconnectEvent.class, e -> clientInfo.remove(e.getPlayer()));

        if (viaProxyDetails) detectors.addChild(VersionDetector.node(clientInfo));
    }

    /** Access client info (e.g. protocol version) from server level detectors */
    public ClientInfoService clientInfo() {
        if (!initialized) throw new IllegalStateException("Mechanics has not been initialized");
        return clientInfo;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
