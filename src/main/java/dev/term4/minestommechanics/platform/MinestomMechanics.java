package dev.term4.minestommechanics.platform;

import dev.term4.minestommechanics.platform.client.ClientInfoService;
import dev.term4.minestommechanics.platform.client.VersionDetector;
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
    private final EventNode<Event> apiEvents = EventNode.all("api-events");

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

        // Root node for all of MinestomMechanics
        MinecraftServer.getGlobalEventHandler().addChild(root);

        // Create child nodes
        EventNode<Event> detectors = EventNode.all("detectors");

        // Add child nodes to root
        root.addChild(detectors);
        root.addChild(apiEvents);

        root.addListener(PlayerDisconnectEvent.class, e -> clientInfo.remove(e.getPlayer()));

        if (viaProxyDetails) detectors.addChild(VersionDetector.node(clientInfo));
    }

    /** Access client info (e.g. protocol version) from server level detectors */
    public ClientInfoService clientInfo() {
        if (!initialized) throw new IllegalStateException("MinestomMechanics has not been initialized");
        return clientInfo;
    }

    /** Public node for MinestomMechanics API events */
    public EventNode<Event> events() {
        if (!initialized) throw new IllegalStateException("MinestomMechanics has not been initialized");
        return apiEvents;
    }

    /** Public method to install a node to the root MinestomMechanics node */
    public void install(EventNode<Event> node) {
        if  (!initialized) throw new IllegalStateException("MinestomMechanics has been initialized");
        root.addChild(node);
    }

    public boolean isInitialized() {
        return initialized;
    }
}
