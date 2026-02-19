package dev.term4.minestommechanics.mechanics.combat.attack;

import dev.term4.minestommechanics.api.event.combat.AttackEvent;
import dev.term4.minestommechanics.mechanics.combat.Combat.Config;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.EntityAttackEvent;

public final class AttackFeature {

    // This class allows the user to install the attack feature and config into the combat module

    private AttackFeature() {}

    /**
     * Install the attack feature with attack config (here's where you can set a custom processor)
     *
     * @param combatNode
     * @param cfg
     */
    public static void install(EventNode<Event> combatNode, Config.Attack cfg, AttackServices services) {

        // register the listener(s) here and forward t cfg.processor.processAttack()
        //  cfg.reach will be important later for validation + swing/raycast attacks

        if (!cfg.enabled) return;

        final AttackProcessor processor = cfg.processor.create(services);

        // Attack packets
        combatNode.addListener(EntityAttackEvent.class, e -> {

            if (!(e.getEntity() instanceof  Player attacker)) return;

            var target = e.getTarget(); // don't filter only LivingEntity. Crystals, armor stands, projectiles, are all nonliving and may have behavior

            // Fire API event
            AttackSnapshot snap = new AttackSnapshot(
                    attacker,
                    target,
                    AttackEvent.Cause.ATTACK_PACKET,
                    attacker.getItemInMainHand(),   // later resolve the hand used
                    services.sprintTracker().isSprintHit(attacker),
                    attacker.getPosition(),
                    target.getPosition()
            );

            AttackEvent api = new AttackEvent(snap);

            MinecraftServer.getGlobalEventHandler().call(api);
            if (api.cancelled()) return;

            // Build snapshot with any API changes
            AttackSnapshot finalSnap = api.toSnapshot();

            // Forward to configured processor
            processor.processAttack(finalSnap, services);
        });

    }

}
