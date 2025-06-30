package pl.ibcgames.smvotifier;

import org.bukkit.plugin.java.JavaPlugin;
import pl.ibcgames.smvotifier.commands.Reload;
import pl.ibcgames.smvotifier.commands.Reward;
import pl.ibcgames.smvotifier.commands.Test;
import pl.ibcgames.smvotifier.commands.Vote;
import pl.ibcgames.smvotifier.modules.Configuration;
import pl.ibcgames.smvotifier.integration.placeholderapi.PlaceholderAPIIntegration;

public final class Votifier extends JavaPlugin {

    private static Configuration config;

    private static Vote vote;
    private static Reward reward;

    @Override
    public void onEnable() {
        config = new Configuration(this);

        if (config.isTokenInvalid()) {
            this.getSLF4JLogger().warn(Consts.NO_IDENTIFIER_MESSAGE_1);
            this.getSLF4JLogger().warn(Consts.NO_IDENTIFIER_MESSAGE_2);
            this.getSLF4JLogger().warn(Consts.NO_IDENTIFIER_MESSAGE_3);
        } else {
            PlaceholderAPIIntegration.register(this);
        }

        this.getCommand(Consts.COMMAND_VOTE_NAME).setExecutor(vote = new Vote(this));
        this.getCommand(Consts.COMMAND_REWARD_NAME).setExecutor(reward = new Reward(this));
        this.getCommand(Consts.COMMAND_TEST_NAME).setExecutor(new Test(this));
        this.getCommand(Consts.COMMAND_RELOAD_NAME).setExecutor(new Reload(this));
    }

    public Configuration getConfiguration() {
        return config;
    }

    public void reloadConfiguration() {
        config.reload();

        vote.reload();
        reward.reload();

        PlaceholderAPIIntegration.reload();
    }
}
