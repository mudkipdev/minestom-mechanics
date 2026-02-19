package dev.term4.minestommechanics.mechanics.combat.knockback;

import dev.term4.minestommechanics.api.event.combat.KnockbackEvent;
import dev.term4.minestommechanics.mechanics.combat.attack.AttackSnapshot;
import dev.term4.minestommechanics.util.VelocityEstimator;
import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public final class DefaultKnockbackSystem implements KnockbackSystem {

    // This is the default (vanilla) knockback system

    private final KnockbackConfig cfg;
    private final EventNode<Event> apiEvents;

    public DefaultKnockbackSystem(KnockbackConfig cfg, EventNode<Event> apiEvents) {
        this.cfg = cfg;
        this.apiEvents = apiEvents;
    }

    @Override
    public void apply(LivingEntity victim, AttackSnapshot snap, KnockbackType type) {
        // Only handle melee attacks for now
        if (type != KnockbackType.ATTACK_PACKET) return;

        // Direction: attacker -> victim (horizontal only for now)
        var attackerPos = snap.attackerPos();
        var victimPos = victim.getPosition();
        double dx = victimPos.x() - attackerPos.x();
        double dz = victimPos.z() - attackerPos.z();

        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist < 1e-6) return;    // replace with degenerate fallbacks

        double dirX = dx / dist;
        double dirZ = dz / dist;

        // Base strength
        double h = cfg.horizontal;
        double v = cfg.vertical;

        // Sprint bonus
        if (snap.sprintHit()) {
            h += cfg.extraHorizontal;
            v += cfg.extraVertical;
        }

        // Friction
        Vec oldVel = victim instanceof Player p ? VelocityEstimator.get(p) : victim.getVelocity();

        // Rewrite simpler and more generally later, do null checks in config, more general vars here (we will need tag overrides)
        double oldX = cfg.horizontalFriction != 0 ? oldVel.x() / cfg.horizontalFriction : 0;
        double oldZ = cfg.horizontalFriction != 0 ? oldVel.z() / cfg.horizontalFriction : 0;
        double oldY = cfg.verticalFriction != 0 ? oldVel.y() / cfg.verticalFriction : 0;

        double newX = oldX + dirX * h * ServerFlag.SERVER_TICKS_PER_SECOND;
        double newZ = oldZ + dirZ * h * ServerFlag.SERVER_TICKS_PER_SECOND;

        double preLimitY = oldY + v;
        double newY = Math.min(preLimitY, cfg.verticalLimit) * ServerFlag.SERVER_TICKS_PER_SECOND;

        Vec result = new Vec(newX, newY, newZ);

        // API
        KnockbackEvent api = new KnockbackEvent(victim, snap, type, result);
        apiEvents.call(api);
        if (api.cancelled()) return;

        victim.setVelocity(api.velocity());

        // Might experiment with forcing a velocity packet for legacy players? Could help combat feel
        //  Would be a compatibility setting

    }

}
