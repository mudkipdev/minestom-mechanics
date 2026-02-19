package dev.term4.minestommechanics.mechanics.combat.knockback.tracking;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.PlayerEvent;

public interface SprintTracker {

    // This interface determines whether a player should be treated as sprinting at the time of an attack
    //  At this time this is ONLY used for knockback, but could be nice for custom combat implementations

    boolean isSprintHit(Player attacker);

    /** Event node for tracking player sprint */
    EventNode<PlayerEvent> node();

}
