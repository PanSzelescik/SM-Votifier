package pl.ibcgames.smvotifier;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import pl.ibcgames.smvotifier.modules.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Utils {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    public static TextComponent message(String message) {
        return LegacyComponentSerializer
                .legacyAmpersand()
                .deserialize(message)
                .asComponent();
    }

    public static <T> T sendRequest(String url, Class<T> classResponse) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            var request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .setHeader("Content-Type", "application/json; utf-8")
                    .setHeader("Accept", "application/json")
                    .timeout(Duration.ofSeconds(5))
                    .version(HttpClient.Version.HTTP_2)
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var body = response.body();

            if (response.statusCode() == 200) {
                return GSON.fromJson(body, classResponse);
            }

            throw new IllegalStateException("Error: " + response.statusCode() + ": " + body);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean sendTokenInvalid(Configuration config, CommandSender sender) {
        if (config.isTokenInvalid()) {
            sender.sendMessage(textComponent(Consts.NO_IDENTIFIER_MESSAGE_1, NamedTextColor.RED));
            sender.sendMessage(textComponent(Consts.NO_IDENTIFIER_MESSAGE_2, NamedTextColor.RED));
            sender.sendMessage(clickableUrlComponent(Consts.NO_IDENTIFIER_MESSAGE_3, NamedTextColor.GREEN));
            return true;
        }
        return false;
    }

    public static void executeCommands(Votifier plugin, CommandSender sender) {
        for (var cmd : plugin.getConfiguration().getCommands()) {
            cmd = cmd.replace(Consts.PLAYER_PLACEHOLDER, sender.getName());
            final var finalCmd = cmd;

            Bukkit.getGlobalRegionScheduler().execute(plugin, () -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
            });
        }
    }

    public static TextComponent textComponent(String text, TextColor color) {
        return Component.text(text)
                .color(color);
    }

    public static TextComponent clickableUrlComponent(String url, TextColor color) {
        return textComponent(url, color)
                .clickEvent(ClickEvent.openUrl(url));
    }
}
