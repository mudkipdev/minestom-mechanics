package dev.term4.minestommechanics.mechanics.combat.attack;

import dev.term4.minestommechanics.api.event.combat.AttackEvent;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record AttackSnapshot (

    // This is essentially a snapshot of all relevant attack information at the time of the attack

    Player attacker,
    @Nullable Entity targetHint,
    AttackEvent.Cause cause,

    // Immutable data for knockback/damage (attack context)
    ItemStack heldItemSnapshot,
    boolean sprintHit,

    // Spatial locations of entities involved
    @Nullable Point attackerPos,
    @Nullable Point targetPos

) {
    public static AttackSnapshot fromPacket(Player attacker, Entity target, boolean sprintHit) {
        return new AttackSnapshot(
                attacker,
                target,
                AttackEvent.Cause.ATTACK_PACKET,
                attacker.getItemInMainHand(),   // update to use the actual item attack was done with
                sprintHit,
                attacker.getPosition(),
                target.getPosition()
        );
    }

}
