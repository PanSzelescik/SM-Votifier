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

public class Reload implements CommandExecutor {

    private final Votifier plugin;

    public Reload(Votifier plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Bukkit.getAsyncScheduler().runNow(this.plugin, (task) -> {
            if (Utils.sendOpRequired(sender)) {
                return;
            }

            this.plugin.reloadConfiguration();

            this.plugin.getSLF4JLogger().info(Consts.CONFIG_RELOADED_MESSAGE);
            sender.sendMessage(Utils.textComponent(Consts.CONFIG_RELOADED_MESSAGE, NamedTextColor.GREEN));

            Utils.sendTokenInvalid(this.plugin.getConfiguration(), sender);
        });

        return true;
    }
}
