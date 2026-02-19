package dev.term4.minestommechanics.api.event.combat;

import dev.term4.minestommechanics.mechanics.combat.attack.AttackSnapshot;
import dev.term4.minestommechanics.mechanics.combat.knockback.KnockbackSystem;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.event.Event;

public final class KnockbackEvent implements Event {

    // This is the public facing API users can hook into to get information or change how a knockback event happens

    private final LivingEntity victim;
    private final AttackSnapshot snapshot;
    private final KnockbackSystem.KnockbackType type;

    private Vec velocity;
    private boolean cancelled;

    public KnockbackEvent(LivingEntity victim, AttackSnapshot snapshot, KnockbackSystem.KnockbackType type, Vec velocity) {
        this.victim = victim;
        this.snapshot = snapshot;
        this.type = type;
        this.velocity = velocity;
    }

    public LivingEntity victim() { return victim; }
    public AttackSnapshot snapshot() { return snapshot; }
    public KnockbackSystem.KnockbackType type() { return type; }

    public Vec velocity() { return velocity; }
    public void velocity(Vec v) { this.velocity = v; }

    public boolean cancelled() { return cancelled; }
    public void cancel() { this.cancelled = true; }

}
