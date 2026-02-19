package dev.term4.minestommechanics.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerTickEvent;
import net.minestom.server.tag.Tag;

/** Position delta velocity estimation for players. Minestom's getVelocity does not work for clients. */
public final class VelocityEstimator {

    private record Frame(Vec velocity, Pos lastPos) {}
    private static final Tag<Frame> FRAME = Tag.Transient("mm:velocity-frame");

    private VelocityEstimator() {}

    /** Install the tick listener. */
    public static void install(EventNode<Event> node) {
        node.addListener(PlayerTickEvent.class, e -> {
            Player player = e.getPlayer();
            Pos current = player.getPosition();
            Frame prev = player.getTag(FRAME);

            Vec velocity = prev != null
                    ? new Vec(current.x() - prev.lastPos().x(), current.y() - prev.lastPos().y(), current.z() - prev.lastPos().z())
                    : Vec.ZERO;
        });
    }

    /** Estimated velocity for a player. */
    public static Vec get(Player player) {
        Frame f = player.getTag(FRAME);
        return f != null ? f.velocity() : Vec.ZERO;
    }

}
