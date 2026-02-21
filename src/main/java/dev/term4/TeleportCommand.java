package dev.term4;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public final class TeleportCommand extends Command {

    public TeleportCommand() {
        super("tp", "teleport");

        var x = ArgumentType.Double("x");
        var y = ArgumentType.Double("y");
        var z = ArgumentType.Double("z");

        setDefaultExecutor((sender, ctx) -> sender.sendMessage("Usage: /tp <x> <y> <z>"));

        addSyntax((sender, ctx) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Only players can use this command.");
                return;
            }
            double xVal = ctx.get(x);
            double yVal = ctx.get(y);
            double zVal = ctx.get(z);
            player.teleport(new Pos(xVal, yVal, zVal));
            player.sendMessage("Teleported to " + xVal + ", " + yVal + ", " + zVal);
        }, x, y, z);
    }
}
