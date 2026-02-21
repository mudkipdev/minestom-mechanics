package dev.term4.minestommechanics.mechanics.combat.attack.rulesets;

import dev.term4.minestommechanics.mechanics.combat.attack.AttackServices;
import dev.term4.minestommechanics.mechanics.combat.attack.AttackSnapshot;
import dev.term4.minestommechanics.mechanics.knockback.KnockbackSystem;
import dev.term4.minestommechanics.mechanics.damage.DamageRequest;
import dev.term4.minestommechanics.mechanics.damage.DamageSystem;
import net.minestom.server.entity.LivingEntity;


import static dev.term4.minestommechanics.mechanics.damage.DamageTypes.PLAYER_ATTACK;

public final class LegacyAttackProcessor implements AttackProcessor {

    private final AttackServices services;

    public LegacyAttackProcessor(AttackServices services) {
        this.services = services;
    }

    @Override
    public void processAttack(AttackSnapshot snap, AttackServices services) {

        // 1. Knockback
        KnockbackSystem kb = services.knockback();
        if (kb != null) {
            kb.apply(snap);
        }

        // 2. Damage
        if (snap.target() instanceof LivingEntity living) {
            DamageSystem damage = services.damage();
            if (damage != null) {
                damage.apply(DamageRequest.of(living, PLAYER_ATTACK)
                        .attacker(snap.attacker())
                        .source(snap.attacker())
                        .amount(1.0f)   // placeholder
                );
            }
        }
    }
}
