package com.yourname.chatrange.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Feature 2: lets anyone with the "chatrange.spy" permission (ops by default) see
 * every private message sent on the server, formatted as:
 *   [Spy] SenderName -> TargetName: the message
 * with player names in aqua and the message content in gold.
 */
public class MessageSpyListener implements Listener {

    // Vanilla commands that send a private message: /msg, /tell, /w, and the
    // long-form /message. First arg after the command is always the target player.
    private static final List<String> WATCHED_COMMANDS = Arrays.asList(
            "/msg", "/tell", "/w", "/message"
    );

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String raw = event.getMessage();
        String[] parts = raw.split(" ");

        // Need at minimum: /command target message
        if (parts.length < 3) {
            return;
        }

        String cmd = parts[0].toLowerCase(Locale.ROOT);
        if (!WATCHED_COMMANDS.contains(cmd)) {
            return;
        }

        Player sender = event.getPlayer();
        String targetName = parts[1];

        // Resolve to the real online player name if we can, otherwise fall back to typed text
        Player targetPlayer = Bukkit.getPlayerExact(targetName);
        String resolvedTargetName = targetPlayer != null ? targetPlayer.getName() : targetName;

        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 2; i < parts.length; i++) {
            if (i > 2) {
                messageBuilder.append(' ');
            }
            messageBuilder.append(parts[i]);
        }
        String privateMessage = messageBuilder.toString();

        Component spyLine = Component.text("[Spy] ", NamedTextColor.GRAY)
                .append(Component.text(sender.getName(), NamedTextColor.AQUA))
                .append(Component.text(" -> ", NamedTextColor.GRAY))
                .append(Component.text(resolvedTargetName, NamedTextColor.AQUA))
                .append(Component.text(": ", NamedTextColor.GRAY))
                .append(Component.text(privateMessage, NamedTextColor.GOLD));

        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.equals(sender)) {
                continue; // don't echo back to the person who sent the /msg
            }
            if (staff.hasPermission("chatrange.spy")) {
                staff.sendMessage(spyLine);
            }
        }

        // Also print to console so it's logged server-side
        Bukkit.getConsoleSender().sendMessage(spyLine);
    }
}
