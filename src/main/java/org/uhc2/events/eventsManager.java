package org.uhc2.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.uhc2.Uhc2;

public class eventsManager {

    public static void registerEvents(Uhc2 main) {
        PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(main.composition, main);
        pluginManager.registerEvents(new PlayerJoinLeave(main), main);
        pluginManager.registerEvents(new PlayerDeath(main), main);
    }

}
