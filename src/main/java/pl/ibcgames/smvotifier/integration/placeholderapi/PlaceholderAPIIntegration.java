package pl.ibcgames.smvotifier.integration.placeholderapi;

import org.bukkit.Bukkit;
import pl.ibcgames.smvotifier.Consts;
import pl.ibcgames.smvotifier.Votifier;

public class PlaceholderAPIIntegration {

    public static SMExpansion expansion;

    public static String getName() {
        return "PlaceholderAPI";
    }

    public static boolean canLoad() {
        return Bukkit.getPluginManager().getPlugin(getName()) != null;
    }

    public static void register(Votifier plugin) {
        if (!canLoad()) return;

        plugin.getSLF4JLogger().info(Consts.PLACEHOLDERAPI_FOUND_MESSAGE);

        expansion = new SMExpansion(plugin);
        expansion.register();
    }

    public static void reload() {
        if (expansion == null) return;

        expansion.reload();
    }
}
