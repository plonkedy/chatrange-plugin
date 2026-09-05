package com.yourname.chatrange.listeners;

import com.yourname.chatrange.ChatRangePlugin;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Feature 1: limits normal chat to a configurable radius.
 * If chat-range is 0, this listener does nothing and chat behaves like vanilla (global).
 */
public class ChatRangeListener implements Listener {

    private final ChatRangePlugin plugin;

    public ChatRangeListener(ChatRangePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncChatEvent event) {
        int range = plugin.getChatRange();

        // 0 = global chat, no filtering needed.
        if (range <= 0) {
            return;
        }

        Player sender = event.getPlayer();
        Location senderLocation = sender.getLocation();
        double maxDistanceSquared = (double) range * (double) range;

        // event.viewers() is a mutable list of everyone who will receive this message.
        // We strip out anyone who is too far away (or in a different world).
        event.viewers().removeIf(audience -> {
            if (!(audience instanceof Player)) {
                // Leave console / other non-player audiences alone
                return false;
            }

            Player viewer = (Player) audience;

            if (viewer.equals(sender)) {
                // Always let the sender see their own message
                return false;
            }

            if (!viewer.getWorld().equals(senderLocation.getWorld())) {
                return true; // different world = too far away
            }

            double distanceSquared = viewer.getLocation().distanceSquared(senderLocation);
            return distanceSquared > maxDistanceSquared;
        });
    }

    // Small helper kept for potential reuse/testing
    private static boolean withinRange(Audience audience, Location origin, double maxDistanceSquared) {
        if (!(audience instanceof Player player)) {
            return true;
        }
        if (!player.getWorld().equals(origin.getWorld())) {
            return false;
        }
        return player.getLocation().distanceSquared(origin) <= maxDistanceSquared;
    }
}
