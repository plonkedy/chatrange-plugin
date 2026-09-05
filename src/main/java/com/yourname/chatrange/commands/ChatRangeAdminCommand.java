package com.yourname.chatrange.commands;

import com.yourname.chatrange.ChatRangePlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * /chatrange reload - reloads config.yml so a changed chat-range value takes effect
 *                      without restarting the server.
 * /chatrange         - shows the currently active range.
 */
public class ChatRangeAdminCommand implements CommandExecutor {

    private final ChatRangePlugin plugin;

    public ChatRangeAdminCommand(ChatRangePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                              @NotNull String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(Component.text(
                    "ChatRange config reloaded. Range is now: " + plugin.describeRange(),
                    NamedTextColor.YELLOW));
            return true;
        }

        sender.sendMessage(Component.text(
                "Current chat range: " + plugin.describeRange() + ". Use /chatrange reload after editing config.yml.",
                NamedTextColor.YELLOW));
        return true;
    }
}
