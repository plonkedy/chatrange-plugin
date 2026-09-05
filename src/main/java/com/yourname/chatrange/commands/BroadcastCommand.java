package com.yourname.chatrange.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Feature 3: /broadcast <message>
 * Sends "[Broadcast]: <message>" to every player on the server.
 * "[Broadcast]:" is dark red, the message itself is light green.
 */
public class BroadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                              @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /broadcast <message>", NamedTextColor.RED));
            return true;
        }

        String message = String.join(" ", args);

        Component broadcastMessage = Component.text("[Broadcast]: ", NamedTextColor.DARK_RED)
                .append(Component.text(message, NamedTextColor.GREEN)); // GREEN = light green in Minecraft's palette

        Bukkit.broadcast(broadcastMessage);
        return true;
    }
}
