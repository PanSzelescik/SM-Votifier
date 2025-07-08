package pl.ibcgames.smvotifier.modules;

import pl.ibcgames.smvotifier.Consts;
import pl.ibcgames.smvotifier.Votifier;

import java.io.File;
import java.util.List;

public class Configuration {

    private final Votifier plugin;

    private String token;
    private boolean requirePermission;
    private List<String> list;

    public Configuration(Votifier plugin) {
        this.plugin = plugin;
        this.reload();
    }

    public void reload() {
        if (!new File(this.plugin.getDataFolder(), "config.yml").exists()) {
            this.plugin.getSLF4JLogger().warn(Consts.NO_CONFIG_MESSAGE);
            this.plugin.saveDefaultConfig();
        }

        this.plugin.reloadConfig();

        var config = this.plugin.getConfig();
        this.token = config.getString(Consts.CONFIG_IDENTIFIER_NAME);
        this.requirePermission = config.getBoolean(Consts.CONFIG_REQUIRE_PERMISSION_NAME);
        this.list = config.getStringList(Consts.CONFIG_COMMANDS_NAME);
    }

    public String getToken() {
        return this.token;
    }

    public boolean requirePermission() {
        return this.requirePermission;
    }

    public List<String> getCommands() {
        return this.list;
    }

    public boolean isTokenInvalid() {
        return this.token == null || this.token.isBlank() || this.token.equalsIgnoreCase(Consts.DEFAULT_IDENTIFIER_VALUE);
    }
}