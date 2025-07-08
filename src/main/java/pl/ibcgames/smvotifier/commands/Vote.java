package pl.ibcgames.smvotifier.commands;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.ibcgames.smvotifier.Consts;
import pl.ibcgames.smvotifier.Utils;
import pl.ibcgames.smvotifier.Votifier;
import pl.ibcgames.smvotifier.response.VoteResponse;

import java.util.List;

public class Vote implements CommandExecutor {

    private final Votifier plugin;

    private List<String> messages;
    private String voteUrl;

    public Vote(Votifier plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        this.plugin.scheduleAsync(() -> {
            try {
                var config = this.plugin.getConfiguration();
                if (Utils.sendTokenInvalid(config, sender)) {
                    return;
                }

                if (voteUrl == null) {
                    sender.sendMessage(Utils.textComponent(Consts.LOADING_DATA_MESSAGE, NamedTextColor.GREEN));
                    var response = Utils.sendRequest(Consts.WEBPAGE_URL + "/api/server-by-key/" + config.getToken() + "/get-vote?folia=1", VoteResponse.class);

                    messages = response.text();
                    voteUrl = response.voteUrl();
                }

                for (var message : messages) {
                    sender.sendMessage(Utils.message(message));
                }
                sender.sendMessage(Utils.clickableUrlComponent(voteUrl, NamedTextColor.YELLOW));
            } catch (Exception e) {
                this.plugin.getSLF4JLogger().warn(Consts.ERROR_DOWNLOAD_VOTE_DATA_MESSAGE, e);
                sender.sendMessage(Utils.textComponent(Consts.ERROR_DOWNLOAD_VOTE_DATA_PLAYER_MESSAGE, NamedTextColor.RED));
            }
        });

        return true;
    }

    public void reload() {
        this.messages = List.of();
        this.voteUrl = null;
    }
}
