package dev.term4.minestommechanics.api.event.damage;

import net.minestom.server.entity.Entity;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.event.Event;
import net.minestom.server.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;

public final class DamageEvent implements Event {

    // This is the public facing API users can hook into to get information or change how a damage event happens

    private final Entity target;
    private final RegistryKey<DamageType> type;
    private @Nullable Entity source;

    private float amount;
    private boolean cancelled;

    public DamageEvent(Entity target, RegistryKey<DamageType> type, @Nullable Entity source, Float amount) {
        this.target = target;
        this.type = type;
        this.source = source;
        this.amount = amount;
    }

    public Entity target() { return target; }
    public RegistryKey<DamageType> type() { return type; }

    public @Nullable Entity source() { return source; }
    public void source(@Nullable Entity source) { this.source = source; }

    public float amount() { return amount; }
    public void amount(float amount) { this.amount = amount; }

    public boolean cancelled() { return cancelled; }
    public void cancel() {  this.cancelled = true; }

}
