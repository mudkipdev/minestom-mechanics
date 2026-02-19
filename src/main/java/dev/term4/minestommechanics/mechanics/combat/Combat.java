package dev.term4.minestommechanics.mechanics.combat;

import dev.term4.minestommechanics.mechanics.combat.attack.AttackServices;
import dev.term4.minestommechanics.mechanics.combat.attack.AttackFeature;
import dev.term4.minestommechanics.mechanics.combat.attack.AttackProcessor;
import dev.term4.minestommechanics.mechanics.combat.knockback.DefaultKnockbackSystem;
import dev.term4.minestommechanics.mechanics.combat.knockback.KnockbackConfig;
import dev.term4.minestommechanics.mechanics.combat.knockback.KnockbackSystem;
import dev.term4.minestommechanics.mechanics.combat.knockback.tracking.PacketSprintTracker;
import dev.term4.minestommechanics.mechanics.combat.knockback.tracking.SprintTracker;
import dev.term4.minestommechanics.mechanics.damage.DamageSystem;
import dev.term4.minestommechanics.MinestomMechanics;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import org.jetbrains.annotations.Nullable;

public final class Combat {

    // This class is basically what the user uses to install different combat behaviors (e.g. Combat.install(attack/blocking/etc)

    private Combat() {}

    public static final class Config {

        public boolean enableAttack = true; // Attack is enabled by default

        public final Attack attack = new Attack();

        // These will likely be replaced with a similar thing as the Attack class below
        public @Nullable KnockbackSystem knockbackSystem = null;
        public final KnockbackConfig knockback = new KnockbackConfig();

        public final Sprint sprint = new Sprint();
        public static final class Sprint {
            public long sprintBuffer = 8;
        }

        public static final class Attack {
            public boolean enabled = true;

            /**
             * Advanced attack processing. Most modification can be done through tags or via AttackEvent.
             * Most users should leave this alone.
             */
            public AttackProcessor.Ruleset processor = AttackProcessor.legacy();

            public double reach = 3.0;
        }
    }

    public static void install(MinestomMechanics mm) {
        install(mm, new Config(), null);
    }

    public static void install(MinestomMechanics mm, Config cfg, @Nullable DamageSystem damage) {
         EventNode<Event> combat = EventNode.all("combat");

         // Combat owned systems
         KnockbackSystem knockback = cfg.knockbackSystem != null
                 ? cfg.knockbackSystem
                 : new DefaultKnockbackSystem(cfg.knockback);   // This is subject to change and can be simplified
         SprintTracker sprintTracker = new PacketSprintTracker(cfg.sprint.sprintBuffer);    // default for now

         AttackServices services = new AttackServices(damage, knockback, sprintTracker);

         if (cfg.enableAttack) {
             AttackFeature.install(combat, cfg.attack, services);
         }

         combat.addChild(sprintTracker.node());

         mm.install(combat);
    }
}
