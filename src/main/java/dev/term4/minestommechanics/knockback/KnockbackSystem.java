package dev.term4.minestommechanics.knockback;

import dev.term4.minestommechanics.MinestomMechanics;
import dev.term4.minestommechanics.api.event.knockback.KnockbackEvent;
import dev.term4.minestommechanics.mechanics.combat.attack.AttackSnapshot;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

/** Knockback system: Config can be changed via KnockbackConfig or the KnockbackEvent API */
public final class KnockbackSystem {

    private final KnockbackConfig config;
    private final EventNode<Event> apiEvents;
    private final KnockbackCalculator calc = new KnockbackCalculator();

    public KnockbackSystem(KnockbackConfig config, EventNode<Event> apiEvents) {
        this.config = config;
        this.apiEvents = apiEvents;
    }

    public void apply(AttackSnapshot snap) {
        var event = new KnockbackEvent(snap, config);
        apiEvents.call(event);
        if (event.cancelled()) return;

        AttackSnapshot finalSnap = event.finalSnap();

        if (finalSnap.target() == null) return;

        Vec velocity;
        if (event.velocity() != null) { velocity = event.velocity(); }
        else {
            velocity = calc.compute(finalSnap, event.config(), event.cause());
            if (event.direction() != null) {
                double mag = Math.sqrt(velocity.x() * velocity.x() + velocity.z() * velocity.z());
                Vec dir = event.direction().normalize();
                velocity = new Vec(dir.x() * mag, velocity.y(), dir.z() * mag);
            }
        }

        finalSnap.target().setVelocity(velocity);
    }

    public static KnockbackSystem install(MinestomMechanics mm, KnockbackConfig config) {
        var system = new KnockbackSystem(config, mm.events());
        mm.registerKnockback(system);
        return system;
    }

}