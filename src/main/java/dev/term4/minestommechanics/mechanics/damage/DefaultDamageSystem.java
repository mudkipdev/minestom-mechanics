package dev.term4.minestommechanics.mechanics.damage;

import dev.term4.minestommechanics.api.event.damage.DamageEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;

public final class DefaultDamageSystem implements DamageSystem {

    // This class is the main damage system. Applies damage, fires API, determines damage type, amount, etc.

    public static final float DEFAULT_AMOUNT = 1.0f;

    @Override
    public void apply(DamageRequest req) {
        Entity target = req.target();
        if (!(target instanceof LivingEntity living)) return;

        float amount = req.amount() != null ? req.amount() : DEFAULT_AMOUNT;

        Entity source = req.source(); // source = projectile / explosion / etc
        Entity attacker = req.attacker(); // attacker = entity responsible (mob, player)

        Point sourcePos = req.sourcePosition();

        // API
        DamageEvent event = new DamageEvent(target, req.type(), req.source(), amount);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.cancelled()) return;

        amount = event.amount();
        if (amount <= 0) return;

        // Build damage
        Damage damage = new Damage(
                req.type(),
                source,
                attacker,
                sourcePos,
                amount
        );

        living.damage(damage);
    }

}
