package dev.term4.minestommechanics.api.event.combat;

import dev.term4.minestommechanics.mechanics.combat.attack.AttackSnapshot;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class AttackEvent implements Event {

    // This is the public facing API users can hook into to get information or change how an attack event happens

    // Was this attack "real" or emulated? Was this a projectile?
    public enum Cause { ATTACK_PACKET, EMULATED_ATTACK, PROJECTILE }

    private final AttackSnapshot snapshot;

    private @Nullable Entity targetOverride;
    private @Nullable Boolean sprintHitOverride;
    private @Nullable ItemStack weaponOverride;

    private boolean cancelled;

    public AttackEvent(AttackSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public AttackSnapshot snapshot() { return snapshot; }

    // immutable
    public Player attacker() { return snapshot.attacker(); }
    public Cause cause() { return snapshot.cause(); }

    // mutable
    public @Nullable Entity target() { return targetOverride !=null ? targetOverride : snapshot.targetHint(); }
    public void target(@Nullable Entity target) { this.targetOverride = target; }

    public boolean sprintHit() { return sprintHitOverride !=null ? sprintHitOverride : snapshot.sprintHit(); }
    public void sprintHit(boolean sprintHit) { this.sprintHitOverride = sprintHit; }

    public ItemStack weapon() { return weaponOverride != null ? weaponOverride : snapshot.heldItemSnapshot(); }
    public void weapon(ItemStack weapon) { this.weaponOverride = weapon; }

    public boolean cancelled() { return cancelled; }
    public void cancel() { this.cancelled = true; }

    /** Build the final snapshot after API modification */
    public AttackSnapshot toSnapshot() {
        Entity t = target();
        return new AttackSnapshot(
                snapshot.attacker(),
                t,
                snapshot.cause(),
                weapon(),
                sprintHit(),
                snapshot.attackerPos(),
                t != null ? t.getPosition() : null
        );
    }

}
