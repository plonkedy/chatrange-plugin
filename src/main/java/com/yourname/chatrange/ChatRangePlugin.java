package com.yourname.chatrange;

import com.yourname.chatrange.commands.BroadcastCommand;
import com.yourname.chatrange.commands.ChatRangeAdminCommand;
import com.yourname.chatrange.listeners.ChatRangeListener;
import com.yourname.chatrange.listeners.MessageSpyListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChatRangePlugin extends JavaPlugin {

    private static ChatRangePlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // Register listeners
        getServer().getPluginManager().registerEvents(new ChatRangeListener(this), this);
        getServer().getPluginManager().registerEvents(new MessageSpyListener(), this);

        // Register commands
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("chatrange").setExecutor(new ChatRangeAdminCommand(this));

        getLogger().info("ChatRange enabled. Current chat range: " + describeRange());
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatRange disabled.");
    }

    /**
     * Reads chat-range from config.yml and clamps it to the valid 0-1000 window.
     * 0 means "no limit" (global chat).
     */
    public int getChatRange() {
        int range = getConfig().getInt("chat-range", 100);
        if (range < 0) {
            range = 0;
        } else if (range > 1000) {
            range = 1000;
        }
        return range;
    }

    public String describeRange() {
        int range = getChatRange();
        return range == 0 ? "unlimited (global chat)" : range + " blocks";
    }

    public static ChatRangePlugin getInstance() {
        return instance;
    }
}
