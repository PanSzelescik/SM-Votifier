package pl.ibcgames.smvotifier.integration.placeholderapi;

import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import pl.ibcgames.smvotifier.Consts;
import pl.ibcgames.smvotifier.Utils;
import pl.ibcgames.smvotifier.Votifier;
import pl.ibcgames.smvotifier.response.GetPluginDetailsResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SMExpansion extends PlaceholderExpansion {

    private final Votifier plugin;
    private long votesCount = 0;
    private Date votesCachedAt = new Date();
    private boolean isPromotionActive = false;
    private Date promotionExpireAt = new Date();
    private Date responseCachedAt = new Date();
    private LocalDateTime lastUpdate = LocalDateTime.now().minusMinutes(5);
    private boolean isFetching = false;

    public SMExpansion(Votifier plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return Consts.PLUGIN_IDENTIFIER;
    }

    @Override
    public @NotNull String getAuthor() {
        return Consts.PLUGIN_AUTHOR;
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return Arrays.asList(
                "votes_count",
                "votes_cached_at",
                "is_promotion_active",
                "promotion_expire_at",
                "response_cached_at");
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        this.saveData();

        return switch (params) {
            case "votes_count" -> String.valueOf(this.votesCount);
            case "votes_cached_at" -> this.votesCachedAt == null ? "" : PlaceholderAPIPlugin.getDateFormat().format(this.votesCachedAt);
            case "is_promotion_active" -> this.isPromotionActive ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
            case "promotion_expire_at" -> this.promotionExpireAt == null ? "" : PlaceholderAPIPlugin.getDateFormat().format(this.promotionExpireAt);
            case "response_cached_at" -> this.responseCachedAt == null ? "" : PlaceholderAPIPlugin.getDateFormat().format(this.responseCachedAt);
            default -> "";
        };
    }

    private void saveData() {
        if (this.isFetching || !LocalDateTime.now().isAfter(this.lastUpdate.plusMinutes(5))) {
            return;
        }

        this.isFetching = true;
        Bukkit.getAsyncScheduler().runNow(plugin, (task) -> {
            try {
                var response = Utils.sendRequest(Consts.WEBPAGE_URL + "/api/server-by-key/" + plugin.getConfiguration().getToken() + "/get-plugin-details", GetPluginDetailsResponse.class);

                votesCount = response.votesCount();
                votesCachedAt = new Date(response.votesCachedAt() * 1000);
                isPromotionActive = response.isPromotionActive();
                promotionExpireAt = new Date(response.promotionExpireAt() * 1000);
                responseCachedAt = new Date(response.responseCachedAt() * 1000);

                this.lastUpdate = LocalDateTime.now();
            }
            catch (Exception e) {
                plugin.getSLF4JLogger().warn(Consts.ERROR_DOWNLOAD_SERVER_DATA_MESSAGE, e);
            }

            this.isFetching = false;
        });
    }

    public void reload() {
        this.votesCount = 0;
        this.votesCachedAt = new Date();
        this.isPromotionActive = false;
        this.promotionExpireAt = new Date();
        this.responseCachedAt = new Date();
        this.lastUpdate = LocalDateTime.now().minusMinutes(5);
        this.isFetching = false;
    }
}
