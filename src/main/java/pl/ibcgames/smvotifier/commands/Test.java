package pl.ibcgames.smvotifier.commands;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.ibcgames.smvotifier.Consts;
import pl.ibcgames.smvotifier.Utils;
import pl.ibcgames.smvotifier.Votifier;

public class Test implements CommandExecutor {

    private final Votifier plugin;

    public Test(Votifier plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        Bukkit.getAsyncScheduler().runNow(this.plugin, (task) -> {
            try {
                if (Utils.sendOpRequired(sender)) {
                    return;
                }

                var config = this.plugin.getConfiguration();
                if (Utils.sendTokenInvalid(config, sender)) {
                    return;
                }

                if (Utils.sendPermissionRequired(config, sender)) {
                    return;
                }

                sender.sendMessage(Utils.textComponent(Consts.TEST_REWARD_MESSAGE_1, NamedTextColor.GREEN));
                sender.sendMessage(Utils.textComponent(Consts.TEST_REWARD_MESSAGE_2, NamedTextColor.GREEN));
                sender.sendMessage(Utils.textComponent(Consts.TEST_REWARD_MESSAGE_3, NamedTextColor.GREEN));

                Utils.executeCommands(this.plugin, sender);
            }
            catch (Exception e) {
                this.plugin.getSLF4JLogger().warn(Consts.ERROR_TEST_REWARD_MESSAGE, e);
                sender.sendMessage(Utils.textComponent(Consts.ERROR_TEST_REWARD_MESSAGE, NamedTextColor.RED));
            }
        });

        return true;
    }
}
