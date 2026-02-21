package dev.term4.minestommechanics.mechanics.combat.attack;

import dev.term4.minestommechanics.mechanics.knockback.KnockbackSystem;
import dev.term4.minestommechanics.util.SprintTracker;
import dev.term4.minestommechanics.mechanics.damage.DamageSystem;
import org.jetbrains.annotations.Nullable;

/**
 * Systems available to attack rulesets for processing attacks
 *
 * @param damage optional damage system (can be null if damage system is not installed)
 * @param knockback optional knockback system (can be null if disabled)
 */
public record AttackServices(
        @Nullable DamageSystem damage,
        @Nullable KnockbackSystem knockback,
        @Nullable SprintTracker sprintTracker
) { }