package dev.term4.minestommechanics.mechanics.combat.knockback.tracking;

import dev.term4.minestommechanics.util.TickClock;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerStopSprintingEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.tag.Tag;

public final class PacketSprintTracker implements SprintTracker {

    // Packet approach for tracking a players sprinting status

    private static final Tag<Long> LAST_SPRINT_TICK = Tag.Transient("last_sprint_tick");

    // "buffer" window for sprint hits (how long should a player be considered as sprinting after they stop actually sprinting)
    private final long sprintBuffer; // wire to config later

    public PacketSprintTracker(long sprintBuffer) {
        this.sprintBuffer = sprintBuffer;
    }

    /** Called when a player starts sprinting */
    public void markStopSprint(Player player) {
        player.setTag(LAST_SPRINT_TICK, TickClock.now());
    }

    @Override
    public boolean isSprintHit(Player attacker) {
        if (attacker.isSprinting()) return true;

        Long last = attacker.getTag(LAST_SPRINT_TICK);
        if (last == null) return false;

        return (TickClock.now() - last) <= sprintBuffer;
    }

    /** Listener node that updates LAST_SPRINT_TICK. */ // Installed in the combat node
    public EventNode<PlayerEvent> node() {
        EventNode<PlayerEvent> node = EventNode.type("sprint-tracker", EventFilter.PLAYER);

        node.addListener(PlayerStopSprintingEvent.class, e -> {
            markStopSprint(e.getPlayer());
        });

        return node;
    }

}
