package pl.ibcgames.smvotifier.modules;

import pl.ibcgames.smvotifier.Consts;
import pl.ibcgames.smvotifier.Votifier;

import java.io.File;
import java.util.List;

public class Configuration {

    private final Votifier plugin;

    private final String token;
    private final boolean requirePermission;
    private final List<String> list;

    public Configuration(Votifier plugin) {
        this.plugin = plugin;
        this.reload();

        var config = plugin.getConfig();
        this.token = config.getString(Consts.CONFIG_IDENTIFIER_NAME);
        this.requirePermission = config.getBoolean(Consts.CONFIG_REQUIRE_PERMISSION_NAME);
        this.list = config.getStringList(Consts.CONFIG_COMMANDS_NAME);
    }

    private void reload() {
        if (!new File(this.plugin.getDataFolder(), "config.yml").exists()) {
            this.plugin.getSLF4JLogger().warn(Consts.NO_CONFIG_MESSAGE);
            this.plugin.saveDefaultConfig();
        }

        this.plugin.reloadConfig();
    }

    public String getToken() {
        return token;
    }

    public boolean requirePermission() {
        return requirePermission;
    }

    public List<String> getCommands() {
        return list;
    }

    public boolean isTokenInvalid() {
        return token == null || token.isBlank() || token.equalsIgnoreCase(Consts.DEFAULT_IDENTIFIER_VALUE);
    }
}