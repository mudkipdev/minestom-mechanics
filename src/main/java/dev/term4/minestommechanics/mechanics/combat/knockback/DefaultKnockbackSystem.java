package dev.term4.minestommechanics.mechanics.combat.knockback;

import dev.term4.minestommechanics.api.event.combat.AttackEvent;
import dev.term4.minestommechanics.mechanics.combat.attack.AttackSnapshot;
import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.network.socket.Server;

public final class DefaultKnockbackSystem implements KnockbackSystem {

    // This is the default (vanilla) knockback system

    private final KnockbackConfig cfg;

    public DefaultKnockbackSystem(KnockbackConfig cfg) {
        this.cfg = cfg;
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
        Vec oldVel = victim.getVelocity();  // replace with velocity estimator (getVelocity doesn't work on players)

        // Rewrite simpler and more generally later, do null checks in config, etc
        double oldX = cfg.horizontalFriction != 0 ? oldVel.x() / cfg.horizontalFriction : 0;
        double oldZ = cfg.horizontalFriction != 0 ? oldVel.z() / cfg.horizontalFriction : 0;
        double oldY = cfg.verticalFriction != 0 ? oldVel.y() / cfg.verticalFriction : 0;

        double newX = oldX + dirX * h * ServerFlag.SERVER_TICKS_PER_SECOND;
        double newZ = oldZ + dirZ * h * ServerFlag.SERVER_TICKS_PER_SECOND;

        double preLimitY = oldY + v;
        double newY = Math.min(preLimitY, cfg.verticalLimit) * ServerFlag.SERVER_TICKS_PER_SECOND;

        Vec result = new Vec(newX, newY, newZ);
        victim.setVelocity(result);

        // Might experiment with forcing a velocity packet for legacy players? Could help combat feel
        //  Would be a compatibility setting

    }

}
