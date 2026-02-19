package dev.term4.minestommechanics.mechanics.combat.knockback;

import dev.term4.minestommechanics.mechanics.combat.attack.AttackSnapshot;
import net.minestom.server.entity.LivingEntity;

public interface KnockbackSystem {

    // This interface applies knockback

    enum KnockbackType {
        ATTACK_PACKET,
        EMULATED_ATTACK,
        PROJECTILE,
        SWEEPING,
        EXPLOSION
    }

    /**
     * Apply knockback to the victim using the provided snapshot
     * @param victim the entity receiving knockback
     * @param snap the finalized attack snapshot
     * @param type what kind of knockback this is
     */
    void apply(LivingEntity victim, AttackSnapshot snap, KnockbackType type);   // might add optional knockback profile object to snapshot later

}
