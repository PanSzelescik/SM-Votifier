package pl.ibcgames.smvotifier.integration.placeholderapi;

import org.bukkit.Bukkit;
import pl.ibcgames.smvotifier.Consts;
import pl.ibcgames.smvotifier.Votifier;

public class PlaceholderAPIIntegration {

    public static String getName() {
        return "PlaceholderAPI";
    }

    public static boolean canLoad() {
        return Bukkit.getPluginManager().getPlugin(getName()) != null;
    }

    public static void register() {
        if (!canLoad()) return;

        Votifier.plugin.getSLF4JLogger().info(Consts.PLACEHOLDERAPI_FOUND_MESSAGE);
        new SMExpansion().register();
    }
}
